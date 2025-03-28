package com.api.v1.alumind.utils;

import com.api.v1.alumind.dtos.reponses.SemanalMetricsDTO;
import java.util.List;

public class EmailsTemplate {

    public String weeklyReportEmailTemplate(SemanalMetricsDTO semanalMetrics, String dataInicial, String dataFinal) {
        return """
                <!DOCTYPE html>
                <html lang="pt-BR">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Relatório Semanal de Feedbacks</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            background-color: #f4f4f4;
                            color: #333;
                        }
                        .container {
                            max-width: 600px;
                            margin: auto;
                            background-color: #fff;
                            padding: 20px;
                            border-radius: 8px;
                            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                        }
                        .header {
                            text-align: center;
                            background-color: #007bff;
                            color: #fff;
                            padding: 20px;
                            border-radius: 8px 8px 0 0;
                        }
                        .content {
                            margin-top: 20px;
                        }
                        .content p {
                            font-size: 16px;
                            line-height: 1.6;
                        }
                        .footer {
                            background-color: #f9f9f9;
                            padding: 10px;
                            text-align: center;
                            color: #777;
                            font-size: 12px;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>Relatório Semanal de Feedbacks</h1>
                        </div>
                        <div class="content">
                            <p><strong>Prezado(a) Stakeholder,</strong></p>
                            <p style="font-size: 16px; line-height: 1.6;">Este é o resumo semanal de feedbacks recebidos da nossa plataforma, referente ao período de <strong>%s</strong> a <strong>%s</strong>. Este relatório inclui as métricas gerais dos feedbacks e as principais funcionalidades sugeridas pelos nossos usuários. A análise das sugestões ajudará a orientar as melhorias que estamos implementando para otimizar a experiência dos usuários. Abaixo, você encontrará as estatísticas detalhadas:</p>

                            <p><strong>Feedbacks totais recebidos:</strong> %s</p>
                            <p><strong>% de feedbacks positivos:</strong> %.2f%%</p>
                            <p><strong>% de feedbacks negativos:</strong> %.2f%%</p>
                            <p><strong>% de feedbacks neutros:</strong> %.2f%%</p>
                            
                            <h2>Principais funcionalidades solicitadas:</h2>
                            <ul>
                                %s
                            </ul>
                            
                            <p>Este é um resumo das principais sugestões que recebemos nesta semana. Agradecemos o feedback de todos e estamos trabalhando para melhorar ainda mais a plataforma!</p>
                        </div>
                        <div class="footer">
                            <p>© 2025 Alumind. Todos os direitos reservados.</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(dataInicial, dataFinal,
                semanalMetrics.getCount(),
                semanalMetrics.getPositivesPercentage(),
                semanalMetrics.getNegativePercentage(),
                semanalMetrics.getNeutralPercentage(),
                generateMainFeaturesList(semanalMetrics.getPrincipalFeatures()));
    }

    private String generateMainFeaturesList(List<String> principalFeatures) {
        StringBuilder sb = new StringBuilder();
        for (String feature : principalFeatures) {
            sb.append("<li>").append(feature).append("</li>");
        }
        return sb.toString();
    }
}
