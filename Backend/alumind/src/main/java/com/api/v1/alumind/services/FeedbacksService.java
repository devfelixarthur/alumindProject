package com.api.v1.alumind.services;

import com.api.v1.alumind.dtos.reponses.*;
import com.api.v1.alumind.dtos.requests.RequestRegisterFeedbackDTO;
import com.api.v1.alumind.entities.Feedback;
import com.api.v1.alumind.entities.RequestedFeature;
import com.api.v1.alumind.enums.Sentiment;
import com.api.v1.alumind.exceptions.BadRequestException;
import com.api.v1.alumind.exceptions.CustomRuntimeException;
import com.api.v1.alumind.exceptions.NotFoundException;
import com.api.v1.alumind.repositories.FeedbackRepository;
import com.api.v1.alumind.repositories.RequestedFeatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class FeedbacksService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private RequestedFeatureRepository requestedFeatureRepository;

    @Autowired
    private LLMService llmService;

    @Transactional
    public ReponseRegisterFeedbackDTO registerFeedback(RequestRegisterFeedbackDTO requestRegisterFeedbackDTO) {
        ResponseLLMDTO responseLLMDTO = llmService.sendFeedbackToLLM(requestRegisterFeedbackDTO.feedback());

        Feedback feedback = new Feedback();
        feedback.setSentiment(responseLLMDTO.getSentiment());
        feedback.setOriginalFeedback(requestRegisterFeedbackDTO.feedback());
        feedback.setDtRegister(LocalDateTime.now());

        Feedback savedFeedback = feedbackRepository.save(feedback);

        List<RequestedFeature> requestedFeatures = responseLLMDTO.getReasons().stream()
                .map(reasons -> {
                    RequestedFeature requestedFeature = new RequestedFeature();
                    requestedFeature.setReason(reasons.getReason());
                    requestedFeature.setCode(reasons.getCode());
                    requestedFeature.setFeedback(savedFeedback);
                    requestedFeature.setDtRegister(LocalDateTime.now());
                    requestedFeatureRepository.save(requestedFeature);

                    return requestedFeature;
                })
                .collect(Collectors.toList());

        savedFeedback.setRequestedFeatures(requestedFeatures);
        feedbackRepository.save(savedFeedback);

        ReponseRegisterFeedbackDTO responseDTO = new ReponseRegisterFeedbackDTO();
        responseDTO.setId(savedFeedback.getId());
        responseDTO.setSentiment(savedFeedback.getSentiment());

        List<RequestedFeatureDTO> requestedFeatureDTOs = requestedFeatures.stream()
                .map(requestedFeature -> {
                    RequestedFeatureDTO dto = new RequestedFeatureDTO();
                    dto.setCode(requestedFeature.getCode());
                    dto.setReason(requestedFeature.getReason());
                    return dto;
                })
                .collect(Collectors.toList());

        responseDTO.setRequestedFeatures(requestedFeatureDTOs);

        return responseDTO;

    }

    @Transactional
    public SemanalMetricsDTO semanalMetrics(String dtStart, String dtEnd) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate startDate;
        LocalDate endDate;

        try {
            startDate = LocalDate.parse(dtStart, formatter);
            endDate = LocalDate.parse(dtEnd, formatter);

            if (startDate.isAfter(endDate)) {
                throw new BadRequestException("Invalid Date Range", "Start date cannot be after end date.");
            }
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Invalid Date Format", "Date must be in dd/MM/yyyy format.");
        }

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        List<Feedback> feedbacks = feedbackRepository.findAllByDtRegisterBetween(startDateTime, endDateTime);
        
        if (feedbacks.isEmpty()){
            throw new NotFoundException("No feedbacks found for the specified date range.");
        }

        Long count = Long.valueOf(feedbacks.size());

        Long positivesFeedbacks = feedbacks.stream().filter(feedback ->
                "POSITIVO".equalsIgnoreCase(String.valueOf(feedback.getSentiment()))
        ).count();

        Long negativeFeedbacks = feedbacks.stream().filter(feedback ->
                "NEGATIVO".equalsIgnoreCase(String.valueOf(feedback.getSentiment()))
        ).count();

        Long neutralFeedbacks = feedbacks.stream().filter(feedback ->
                "NEUTRO".equalsIgnoreCase(String.valueOf(feedback.getSentiment()))
        ).count();

        Double positivesPercentage = (count > 0) ? roundToFourDecimalPlaces((positivesFeedbacks * 100.0 / count)) : 0.0;
        Double negativePercentage = (count > 0) ? roundToFourDecimalPlaces((negativeFeedbacks * 100.0 / count)) : 0.0;
        Double neutralPercentage = (count > 0) ? roundToFourDecimalPlaces((neutralFeedbacks * 100.0 / count)) : 0.0;

        List<RequestedFeatureDTO> requestedFeaturesDTO = feedbacks.stream()
                .flatMap(feedback -> feedback.getRequestedFeatures().stream())
                .map(rf -> new RequestedFeatureDTO(rf.getCode(), rf.getReason()))
                .collect(Collectors.toList());

        List<RequestedFeatureDTO> features = requestedFeaturesDTO.stream()
                .collect(Collectors.groupingBy(RequestedFeatureDTO::getCode))
                .entrySet().stream()
                .map(entry -> entry.getValue().stream()
                        .reduce((a, b) -> a.getReason().compareTo(b.getReason()) > 0 ? a : b)
                        .get())
                .collect(Collectors.toList());

        SemanalMetricsDTO semanalMetricsDTO = new SemanalMetricsDTO();
        semanalMetricsDTO.setCount(count);
        semanalMetricsDTO.setPositivesFeedbacks(positivesFeedbacks);
        semanalMetricsDTO.setNegativeFeedbacks(negativeFeedbacks);
        semanalMetricsDTO.setNeutralFeedbacks(neutralFeedbacks);
        semanalMetricsDTO.setPositivesPercentage(positivesPercentage);
        semanalMetricsDTO.setNegativePercentage(negativePercentage);
        semanalMetricsDTO.setNeutralPercentage(neutralPercentage);
        semanalMetricsDTO.setFeatures(features);

        List<String> principalFeatures = llmService.analysePrincipalFeatures(features);

        semanalMetricsDTO.setPrincipalFeatures(principalFeatures);

        return semanalMetricsDTO;
    }

    private Double roundToFourDecimalPlaces(Double value) {
        BigDecimal bigDecimal = new BigDecimal(value);
        bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }


    @Transactional
    public ReponseFeedbascksbyFieldsDTO searchFeedbacksByFields(Long id, String sentiment, Integer size, Integer page) {
        if (size > 100 || size <= 0) {
            throw new BadRequestException("Size must be a number between 1 and 100.");
        }

        Pageable pageable = PageRequest.of(page, size);

        Sentiment sentimentEnum = null;
        if (sentiment != null && !sentiment.trim().isEmpty()) {
            try {
                sentimentEnum = Sentiment.valueOf(sentiment.trim().toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Sentiment must be one of the following values: POSITIVO, NEGATIVO, or NEUTRO.");
            }
        }



        Page<Feedback> feddbacksPage = feedbackRepository.findFeedbacksByIdAndSentiment(id, sentimentEnum, pageable);

        Optional.ofNullable(feddbacksPage)
                .filter(p -> !p.isEmpty())
                .ifPresentOrElse(p -> {}, () -> {throw new NotFoundException("Data not found for the specified parameters.");});

        List<FeedbackDTO> feedbacksDTO = feddbacksPage.getContent().stream()
                .map(feedback -> new FeedbackDTO(feedback))
                .collect(Collectors.toList());

        ReponseFeedbascksbyFieldsDTO result = new ReponseFeedbascksbyFieldsDTO();
        InfoPage infoPage = new InfoPage();

        result.setFeedbacks(feedbacksDTO);
        infoPage.setPageNumber(feddbacksPage.getNumber());
        infoPage.setPageSize(feddbacksPage.getSize());
        infoPage.setTotalPages(feddbacksPage.getTotalPages());
        infoPage.setTotalRecords(feddbacksPage.getTotalElements());
        infoPage.setHasPreviousPage(feddbacksPage.hasPrevious());
        infoPage.setHasNextPage(feddbacksPage.hasNext());
        result.setInfoPage(infoPage);

        return result;

    }
}
