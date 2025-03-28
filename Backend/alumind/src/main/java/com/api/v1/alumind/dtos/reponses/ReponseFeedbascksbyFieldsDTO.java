package com.api.v1.alumind.dtos.reponses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReponseFeedbascksbyFieldsDTO {
    private InfoPage infoPage;
    private List<FeedbackDTO> feedbacks;
}
