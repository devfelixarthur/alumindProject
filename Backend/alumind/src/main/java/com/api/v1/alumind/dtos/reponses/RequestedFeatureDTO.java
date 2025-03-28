package com.api.v1.alumind.dtos.reponses;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestedFeatureDTO {
    private String code;
    private String reason;
}
