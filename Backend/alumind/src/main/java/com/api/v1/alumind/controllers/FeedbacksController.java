package com.api.v1.alumind.controllers;

import com.api.v1.alumind.dtos.reponses.FeedbackDTO;
import com.api.v1.alumind.dtos.reponses.ReponseFeedbascksbyFieldsDTO;
import com.api.v1.alumind.dtos.reponses.ReponseRegisterFeedbackDTO;
import com.api.v1.alumind.dtos.reponses.SemanalMetricsDTO;
import com.api.v1.alumind.dtos.requests.RequestRegisterFeedbackDTO;
import com.api.v1.alumind.entities.Feedback;
import com.api.v1.alumind.services.FeedbacksService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedbacks")
public class FeedbacksController {
    @Autowired
    private FeedbacksService feedbacksService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ReponseRegisterFeedbackDTO> registerFeedback(@RequestBody @Valid RequestRegisterFeedbackDTO requestRegisterFeedbackDTO) {
        return ResponseEntity.ok(feedbacksService.registerFeedback(requestRegisterFeedbackDTO));
    }

    @GetMapping("/searchFeedbacksByFields")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ReponseFeedbascksbyFieldsDTO> registerFeedback(
              @RequestParam(required = false) Long id,
              @RequestParam(required = false) String sentiment,
              @RequestParam (defaultValue = "30") Integer size,
              @RequestParam (defaultValue = "0") Integer page,
              @RequestParam (required = false) String dtStart,
              @RequestParam (required = false) String dtEnd) {
        return ResponseEntity.ok(feedbacksService.searchFeedbacksByFields(id, sentiment, size, page, dtStart, dtEnd));
    }

    @GetMapping("/semanalMetrics")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SemanalMetricsDTO> registerFeedback(@RequestParam String dtStart, @RequestParam String dtEnd) {
        return ResponseEntity.ok(feedbacksService.semanalMetrics(dtStart, dtEnd));
    }

    @GetMapping("/feedbackDetails/{id}")
    @ResponseBody
    public FeedbackDTO getFeedbackDetails(@PathVariable Long id) {
        return feedbacksService.getFeedbackDetails(id);
    }

}
