    package com.api.v1.alumind.dtos.reponses;

    import com.api.v1.alumind.enums.Sentiment;
    import lombok.*;

    import java.util.List;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Getter
    @Setter
    public class ResponseLLMDTO {
        private Sentiment sentiment;
        private String code;
        private List<RequestedFeatureDTO> reasons;
    }
