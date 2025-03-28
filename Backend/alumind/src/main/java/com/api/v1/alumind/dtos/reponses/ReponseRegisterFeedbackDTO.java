package com.api.v1.alumind.dtos.reponses;

import com.api.v1.alumind.enums.Sentiment;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReponseRegisterFeedbackDTO {
    private Long id;
    private Sentiment sentiment;
    private List<RequestedFeatureDTO> requestedFeatures;
}
