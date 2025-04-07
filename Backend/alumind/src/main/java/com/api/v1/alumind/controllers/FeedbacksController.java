package com.api.v1.alumind.controllers;

import com.api.v1.alumind.dtos.reponses.FeedbackDTO;
import com.api.v1.alumind.dtos.reponses.ReponseFeedbascksbyFieldsDTO;
import com.api.v1.alumind.dtos.reponses.ReponseRegisterFeedbackDTO;
import com.api.v1.alumind.dtos.reponses.SemanalMetricsDTO;
import com.api.v1.alumind.dtos.requests.RequestRegisterFeedbackDTO;
import com.api.v1.alumind.services.FeedbacksService;
import com.api.v1.alumind.utils.ResponsePadraoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;

@RestController
@RequestMapping("/feedbacks")
public class FeedbacksController {
    @Autowired
    private FeedbacksService feedbacksService;


    @Operation(summary = "Cadastrar um feedback")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Feedback cadastrado com sucesso.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReponseRegisterFeedbackDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponsePadraoDTO.class))
            )})
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ReponseRegisterFeedbackDTO> registerFeedback(@RequestBody @Valid RequestRegisterFeedbackDTO requestRegisterFeedbackDTO) {
        return ResponseEntity.ok(feedbacksService.registerFeedback(requestRegisterFeedbackDTO));
    }

    @Operation(summary = "Buscar por feedbackd")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado com sucesso.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReponseFeedbascksbyFieldsDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponsePadraoDTO.class))
            ),
            @ApiResponse(responseCode = "404", description = "Dados não encontrados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponsePadraoDTO.class))
            )})
    @GetMapping("/searchFeedbacksByFields")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ReponseFeedbascksbyFieldsDTO> registerFeedback(
              @RequestParam(required = false) Long id,
              @RequestParam(required = false) String sentiment,
              @RequestParam (defaultValue = "30") Integer size,
              @RequestParam (defaultValue = "0") Integer page,
              @RequestParam (required = false, defaultValue = "") String dtStart,
              @RequestParam (required = false, defaultValue = "") String dtEnd) {
        return ResponseEntity.ok(feedbacksService.searchFeedbacksByFields(id, sentiment, size, page, dtStart, dtEnd));
    }

    @Operation(summary = "Buscar Métricas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado com sucesso.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SemanalMetricsDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponsePadraoDTO.class))
            ),
            @ApiResponse(responseCode = "404", description = "Dados não encontrados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponsePadraoDTO.class))
            )})
    @GetMapping("/semanalMetrics")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SemanalMetricsDTO> registerFeedback(@RequestParam String dtStart, @RequestParam String dtEnd) {
        return ResponseEntity.ok(feedbacksService.semanalMetrics(dtStart, dtEnd));
    }

    @Operation(summary = "Buscar detalhes de um feedback específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado com sucesso.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FeedbackDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponsePadraoDTO.class))
            ),
            @ApiResponse(responseCode = "404", description = "Dados não encontrados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponsePadraoDTO.class))
            )})
    @GetMapping("/feedbackDetails/{id}")
    @ResponseBody
    public FeedbackDTO getFeedbackDetails(@PathVariable Long id) {
        return feedbacksService.getFeedbackDetails(id);
    }

}
