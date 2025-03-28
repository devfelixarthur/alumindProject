package com.api.v1.alumind.dtos.reponses;

import com.api.v1.alumind.entities.RequestedFeature;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SemanalMetricsDTO {
    private Long count;
    private Long positivesFeedbacks;
    private Long negativeFeedbacks;
    private Long neutralFeedbacks;
    private Double positivesPercentage;
    private Double negativePercentage;
    private Double neutralPercentage;
    private List<String> principalFeatures;
    private List<RequestedFeatureDTO> features;
}
