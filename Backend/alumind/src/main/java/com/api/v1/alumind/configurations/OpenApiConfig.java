package com.api.v1.alumind.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Alumind Avalidação de Feedbacks")
                        .description("API para integração de análise de feedbacks da plataforma Alumind, com análise de feedback feito por LLM. A Api consiste em receber um feedbacks, a LLM analisa e classifica estes comentários.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("API Alumind")
                                .email("dev.felixarthur@gmail.com")));
    }
}


