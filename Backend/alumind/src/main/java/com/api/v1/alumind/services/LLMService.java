package com.api.v1.alumind.services;

import com.api.v1.alumind.dtos.reponses.RequestedFeatureDTO;
import com.api.v1.alumind.dtos.reponses.ResponseLLMDTO;
import com.api.v1.alumind.enums.Sentiment;
import com.api.v1.alumind.exceptions.BadGatewayException;
import com.api.v1.alumind.exceptions.BadRequestException;
import com.api.v1.alumind.repositories.RequestedFeatureRepository;
import com.api.v1.alumind.utils.PromptUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LLMService {
    @Autowired
    RequestedFeatureRepository requestedFeatureRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${llm.router}")
    private String apiUrl;

    public ResponseLLMDTO sendFeedbackToLLM(String feedback) {
        String promptAnalysis = PromptUtils.getPromptAnalysis(feedback);
        String jsonResponse = sendRequestToLlm(promptAnalysis);
        return parseJsonResponse(jsonResponse);
    }

    public String sendRequestToLlm(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> content = new HashMap<>();
        content.put("text", prompt);
        requestBody.put("contents", List.of(Map.of("parts", List.of(content))));
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                throw new RuntimeException("Error calling Gemini API: " + response.getStatusCode() + " - " + response.getBody());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error sending request to Gemini API", e);
        }
    }

    public List<String> analysePrincipalFeatures(List<RequestedFeatureDTO> features) {
        String promptAnalysis = PromptUtils.getPromptAnalysisFeatures(features);
        String jsonResponse = sendRequestToLlm(promptAnalysis);
        return parseJsonResponsePrincipalFeatures(jsonResponse);
    }

    private ResponseLLMDTO parseJsonResponse(String jsonResponse) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode candidatesNode = rootNode.path("candidates");

            if (candidatesNode.isArray() && candidatesNode.size() > 0) {
                JsonNode contentNode = candidatesNode.get(0).path("content");

                JsonNode partsNode = contentNode.path("parts");
                if (partsNode.isArray() && partsNode.size() > 0) {
                    String content = partsNode.get(0).path("text").asText();

                    content = content.replace("```json", "").replace("```", "").trim();

                    if (content == null || content.isEmpty() || content.trim().toLowerCase().contains("não foi possível interpretar")) {
                        throw new BadRequestException("Não foi possível interpretar o feedback fornecido. Para nos ajudar a entender suas necessidades, por favor, forneça exemplos concretos e descrições detalhadas de suas sugestões ou problemas. Por exemplo: 'Gostaria que a função X fosse melhorada porque...' ou 'Encontrei um problema ao usar a função Y...'");
                    }


                    JsonNode contentJson = objectMapper.readTree(content);

                    JsonNode suggestionsNode = contentJson.path("suggestions");
                    JsonNode sentimentNode = contentJson.path("sentiment");

                    String sentiment = sentimentNode.asText();

                    List<RequestedFeatureDTO> requestedFeatures = new ArrayList<>();

                    if (suggestionsNode.isArray()) {
                        for (JsonNode suggestionNode : suggestionsNode) {
                            String reason = suggestionNode.path("reason").asText();
                            String code = suggestionNode.path("code").asText();

                            if (requestedFeatureRepository.existsByCode(code)) {
                                code = generateUniqueCodeForReasonGEMINI(reason);
                            }

                            RequestedFeatureDTO dto = new RequestedFeatureDTO();
                            dto.setCode(code);
                            dto.setReason(reason);
                            requestedFeatures.add(dto);
                        }
                    }

                    Sentiment sentimentEnum = mapSentimentToEnum(sentiment);

                    ResponseLLMDTO llmDTO = new ResponseLLMDTO();
                    llmDTO.setReasons(requestedFeatures);
                    llmDTO.setSentiment(sentimentEnum);
                    return llmDTO;
                }
            }

            throw new BadRequestException("The LLM response is not in the expected format. Response: " + jsonResponse);
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BadGatewayException("Error processing the LLM API response", e);
        }
    }

    public List<String> parseJsonResponsePrincipalFeatures(String jsonResponse) {
        List<String> principalFeatures = new ArrayList<>();
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode candidatesNode = rootNode.path("candidates");

            if (candidatesNode.isArray() && candidatesNode.size() > 0) {
                JsonNode contentNode = candidatesNode.get(0).path("content");
                JsonNode partsNode = contentNode.path("parts");

                if (partsNode.isArray() && partsNode.size() > 0) {
                    String content = partsNode.get(0).path("text").asText();

                    content = content.replace("`json", "").replace("`", "").trim();

                    JsonNode contentJson = objectMapper.readTree(content);

                    if (contentJson.isArray()) {
                        for (JsonNode featureNode : contentJson) {
                            JsonNode patternNode = featureNode.get("padrão");
                            JsonNode weight = featureNode.get("peso");
                            if (patternNode != null && patternNode.isTextual()) {
                                principalFeatures.add(patternNode.asText() + "(Peso: " + weight.asText() + ")");
                            }
                        }
                    } else {
                        throw new BadRequestException("The LLM response is not in the expected format. Expected an array.");
                    }
                } else {
                    throw new BadRequestException("The LLM response is not in the expected format. Missing 'parts' array.");
                }
            } else {
                throw new BadRequestException("The LLM response is not in the expected format. Missing 'candidates' array.");
            }
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BadGatewayException("Error processing the LLM API response", e);
        }

        return principalFeatures;
    }

    @Transactional
    public String generateUniqueCodeForReasonGEMINI(String reason) {
        String generatedCode = "";
        while (requestedFeatureRepository.existsByCode(generatedCode) || generatedCode.isEmpty()) {
            String promptCode = PromptUtils.getPromptForNewCode(reason);
            String newCodeResponse = sendRequestToLlm(promptCode);
            generatedCode = extractCodeFromResponse(newCodeResponse);
        }

        return generatedCode;
    }

    private Sentiment mapSentimentToEnum(String sentiment) {
        switch (sentiment.toUpperCase()) {
            case "POSITIVO":
                return Sentiment.POSITIVO;
            case "NEGATIVO":
                return Sentiment.NEGATIVO;
            case "NEUTRO":
                return Sentiment.NEUTRO;
            case "INCONCLUSIVO":
                return Sentiment.INCONCLUSIVO;
            default:
                throw new BadRequestException("Sentiment value is not recognized: " + sentiment);
        }
    }

    private String extractCodeFromResponse(String response) {
        try {
            JsonNode rootNode = objectMapper.readTree(response);

            JsonNode candidatesNode = rootNode.path("candidates");

            if (candidatesNode.isArray() && candidatesNode.size() > 0) {
                JsonNode contentNode = candidatesNode.get(0).path("content");
                JsonNode partsNode = contentNode.path("parts");

                if (partsNode.isArray() && partsNode.size() > 0) {
                    String text = partsNode.get(0).path("text").asText().trim();

                    text = text.replaceAll("\n", "").trim();

                    return text;
                }
            }

            throw new BadRequestException("Error: The generated code was not found in the AI response.");
        } catch (Exception e) {
            throw new BadGatewayException("Error processing the AI response", e);
        }
    }
}
