package com.api.v1.alumind.dtos.reponses;

import com.api.v1.alumind.entities.Feedback;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDTO {
    private Long id;
    private String originalFeedback;
    private String sentiment;
    private List<RequestedFeatureDTO> requestedFeatures;
    private String dtRegister;

    public FeedbackDTO(Feedback feedback) {
        this.id = feedback.getId();
        this.originalFeedback = feedback.getOriginalFeedback();
        this.sentiment = String.valueOf(feedback.getSentiment());

        this.requestedFeatures = feedback.getRequestedFeatures().stream()
                .map(rf -> new RequestedFeatureDTO(rf.getCode(), rf.getReason()))
                .collect(Collectors.toList());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        if(feedback.getDtRegister() != null){
            this.dtRegister = feedback.getDtRegister().format(formatter);
        }
    }

}
