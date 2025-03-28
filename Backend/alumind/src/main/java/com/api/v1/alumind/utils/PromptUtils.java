package com.api.v1.alumind.utils;

public class PromptUtils {
    public static String getPromptAnalysis(String feedback) {
        return String.format(
                "Você é um analista experiente em análise de feedback de usuários e geração de sugestões de funcionalidades. Sua tarefa é analisar o feedback a seguir e gerar uma ou mais sugestões de melhorias claras e objetivas, com base na avaliação do usuário. Cada sugestão deve incluir um código único e uma razão explicando o porquê a sugestão é importante.\n\n" +
                        "Feedback: %s\n\n" +
                        "Por favor, siga as instruções abaixo:\n" +
                        "1. Gere um código único para cada sugestão de funcionalidades. Cada código deve ter o formato 'PALAVRA_SEGUNDAPALAVRA' e não deve ultrapassar 75 caracteres. O código deve ter uma associação profunda com a sugestão, a palavra-chave de maior peso.\n" +
                        "2. Para cada sugestão, forneça uma razão que resuma diretamente o que o usuário está sugerindo ou solicitando. A razão deve ser objetiva e ter um máximo de 100 caracteres.\n" +
                        "3. Classifique o sentimento do feedback como 'POSITIVO', 'NEGATIVO', 'NEUTRO' ou 'INCONCLUSIVO'. O sentimento é crucial para entender a percepção do usuário e deverá ser gerado a partir do feedback enviado.\n" +
                        "4. Caso o feedback fornecido seja irrelevante, sem sentido ou desconexo, retorne a seguinte mensagem em formato JSON:\n\n" +
                        "{\n" +
                        "  \"mensagem\": \"Não foi possível interpretar o feedback fornecido. Para nos ajudar a entender suas necessidades, por favor, forneça exemplos concretos e descrições detalhadas de suas sugestões ou problemas. Exemplo: 'Gostaria que a função X fosse melhorada porque...' ou 'Encontrei um problema ao usar a função Y...'\n" +
                        "}\n" +
                        "5. Retorne uma lista de sugestões de funcionalidades, com seu respectivo código e razão. Caso haja múltiplas sugestões, elas devem ser retornadas como um array de objetos, como exemplificado abaixo:\n" +
                        "[\n" +
                        "  \"sentiment\": \"[Generated Sentiment]\"\n" +
                        "    {\n" +
                        "        \"code\": \"[Generated Code]\",\n" +
                        "        \"reason\": \"[Generated Reason]\",\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"code\": \"[Generated Code 2]\",\n" +
                        "        \"reason\": \"[Generated Reason 2]\",\n" +
                        "    }\n" +
                        "]\n" +
                        "Por favor, seja assertivo e decisivo na análise, sempre mantendo a objetividade e clareza em suas sugestões. Lembre-se de que você é um analista experiente que consegue perceber rapidamente as necessidades do usuário e traduzir isso em melhorias efetivas e relevantes. Responda as sugestões e a classificação de sentimento em português (PT-BR).",
                feedback
        );
    }

    public static String getPromptForNewCode(String reason) {
        return String.format(
                "Você está continuando a tarefa de gerar um código único para uma sugestão de funcionalidade. " +
                        "A razão abaixo já foi fornecida, e um código já foi gerado. " +
                        "No entanto, o código já existe no sistema, então, por favor, gere um novo código único.\n\n" +
                        "Razão: %s\n\n" +
                        "Por favor, forneça apenas um novo código único no formato 'PALAVRA_SEGUNDAPALAVRA_TERCEIRAPALAVRA' as palavras devem ser completas. " +
                        "Não deve ultrapassar 75 caracteres. Não gere uma nova razão, apenas forneça o novo código.\n" +
                        "Retorne apenas o novo código como uma string, nada mais.",
                reason
        );
    }
}
