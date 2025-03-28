    package com.api.v1.alumind.services;

    import com.api.v1.alumind.dtos.reponses.RequestedFeatureDTO;
    import com.api.v1.alumind.dtos.reponses.ResponseLLMDTO;
    import com.api.v1.alumind.enums.Sentiment;
    import com.api.v1.alumind.exceptions.BadGatewayException;
    import com.api.v1.alumind.exceptions.BadRequestException;
    import com.api.v1.alumind.repositories.RequestedFeatureRepository;
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
        @Value("${llm.token}")
        private String apiToken;
        @Value("${llm.model}")
        private String apiModel;

        public ResponseLLMDTO sendFeedbackToLLM(String feedback) {
            String promptAnalisys = String.format(
                    "You are an expert in analyzing user feedback and generating feature suggestions. Analyze the following feedback and provide one or more suggestions for improvement based on the feedback. Each suggestion must include a unique code and a reason.\n\n" +
                            "Feedback: %s\n\n" +
                            "Please follow these instructions:\n" +
                            "1. Generate one or more unique codes for the feature suggestions. Each code should be in the format 'WORD_SECONDWORD' and should not exceed 75 characters.\n" +
                            "2. Provide a reason for each suggestion that explains why it is important. The reason should not exceed 500 characters.\n" +
                            "3. Classify the sentiment of the feedback as 'POSITIVE', 'NEGATIVE', or 'NEUTRAL' it is very important.\n" +
                            "4. Return a list of suggestions, each with its own code and reason. If there are multiple suggestions, return them as an array of objects, like this:\n" +
                            "[\n" +
                            "    {\n" +
                            "        \"code\": \"[Generated Code]\",\n" +
                            "        \"reason\": \"[Generated Reason]\",\n" +
                            "        \"sentiment\": \"[Generated Sentiment]\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "        \"code\": \"[Generated Code 2]\",\n" +
                            "        \"reason\": \"[Generated Reason 2]\",\n" +
                            "        \"sentiment\": \"[Generated Sentiment 2]\"\n" +
                            "    }\n" +
                            "]\n" +
                            "Make sure the generated codes, reasons, and sentiment are relevant to the feedback and follow the length restrictions.",
                    feedback
            );

            String jsonResponse = sendRequestToLlm(promptAnalisys);
            return parseJsonResponse(jsonResponse);
        }


        private String sendRequestToLlm(String prompt) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiToken);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", apiModel);

            Map<String, Object> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", prompt);

            requestBody.put("messages", new Map[]{message});

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            try {
                ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

                if (response.getStatusCode() == HttpStatus.OK) {
                    return response.getBody();
                } else {
                    throw new BadRequestException("Error calling LLM API: " + response.getStatusCode() + " - " + response.getBody());
                }
            } catch (Exception e) {
                throw new BadGatewayException("Error sending request to LLM API", e);
            }
        }

        private ResponseLLMDTO parseJsonResponse(String jsonResponse) {
            try {
                JsonNode rootNode = objectMapper.readTree(jsonResponse);
                JsonNode choicesNode = rootNode.path("choices");

                if (choicesNode.isArray() && choicesNode.size() > 0) {
                    JsonNode messageNode = choicesNode.get(0).path("message");
                    if (messageNode.isObject()) {
                        String content = messageNode.path("content").asText();

                        List<RequestedFeatureDTO> requestedFeatures = new ArrayList<>();
                        String sentiment = "";

                        String[] suggestions = content.split("\n");

                        for (String suggestion : suggestions) {
                            if (suggestion.startsWith("code:") && suggestion.contains("reason:") && suggestion.contains("sentiment:")) {
                                String code = suggestion.substring(suggestion.indexOf("code:") + 5, suggestion.indexOf("reason:")).trim();
                                String reason = suggestion.substring(suggestion.indexOf("reason:") + 7, suggestion.indexOf("sentiment:")).trim();
                                sentiment = suggestion.substring(suggestion.indexOf("sentiment:") + 10).trim();


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
            } catch (Exception e) {
                throw new BadGatewayException("Error processing the LLM API response", e);
            }
        }

        @Transactional
        public String generateUniqueCodeForReason(String reason) {
            ResponseLLMDTO responseLLMDTO = sendFeedbackToLLM(reason);
            String generatedCode = responseLLMDTO.getReasons().get(0).getCode();
            while (requestedFeatureRepository.existsByCode(generatedCode)) {
                String promptForNewCode = String.format(
                        "You are continuing the task of generating a unique code for a feature suggestion. The reason below has already been provided, and a code has been generated. However, the code already exists in the system, so please generate a new unique code.\n\n" +
                                "Reason: %s\n\n" +
                                "Please provide only a new unique code in the format 'WORD_SECONDWORD'. It should not exceed 75 characters. Do not generate a new reason, only provide the new code.\n" +
                                "Return the new code as a string, nothing else.",
                        reason
                );
                responseLLMDTO = sendFeedbackToLLM(promptForNewCode);
                generatedCode = responseLLMDTO.getReasons().get(0).getCode();
            }
            return generatedCode;
        }

        private Sentiment mapSentimentToEnum(String sentiment) {
            switch (sentiment.toUpperCase()) {
                case "POSITIVE":
                    return Sentiment.POSITIVO;
                case "NEGATIVE":
                    return Sentiment.NEGATIVO;
                case "NEUTRAL":
                    return Sentiment.INCONCLUSIVO;
                default:
                    throw new BadRequestException("Sentiment value is not recognized: " + sentiment);
            }
        }

    }
