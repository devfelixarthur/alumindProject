package com.api.v1.alumind.services;

import com.api.v1.alumind.dtos.reponses.ReponseRegisterFeedbackDTO;
import com.api.v1.alumind.dtos.reponses.RequestedFeatureDTO;
import com.api.v1.alumind.dtos.reponses.ResponseLLMDTO;
import com.api.v1.alumind.dtos.requests.RequestRegisterFeedbackDTO;
import com.api.v1.alumind.entities.Feedback;
import com.api.v1.alumind.entities.RequestedFeature;
import com.api.v1.alumind.repositories.FeedbackRepository;
import com.api.v1.alumind.repositories.RequestedFeatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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


}
