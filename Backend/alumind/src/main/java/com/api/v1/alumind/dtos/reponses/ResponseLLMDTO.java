    package com.api.v1.alumind.dtos.reponses;

    import com.api.v1.alumind.enums.Sentiment;
    import lombok.*;

    import java.util.List;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public class ResponseLLMDTO {
        private Sentiment sentiment;
        private List<RequestedFeatureDTO> reasons;
    }
