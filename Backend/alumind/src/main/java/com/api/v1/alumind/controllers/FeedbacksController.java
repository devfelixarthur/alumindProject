package com.api.v1.alumind.controllers;

import com.api.v1.alumind.dtos.reponses.ReponseRegisterFeedbackDTO;
import com.api.v1.alumind.dtos.requests.RequestRegisterFeedbackDTO;
import com.api.v1.alumind.services.FeedbacksService;
import com.api.v1.alumind.utils.ResponsePadraoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedbacks")
public class FeedbacksController {
    @Autowired
    private FeedbacksService feedbacksService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ReponseRegisterFeedbackDTO> registerFeedback(@RequestBody RequestRegisterFeedbackDTO requestRegisterFeedbackDTO) {
        return ResponseEntity.ok(feedbacksService.registerFeedback(requestRegisterFeedbackDTO));
    }
}
