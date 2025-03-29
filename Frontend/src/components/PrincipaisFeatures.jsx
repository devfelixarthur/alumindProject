import {
  Box,
  Heading,
  Stack,
  Table,
  Button,
  Flex,
  Text
} from "@chakra-ui/react";
import { useState } from "react";

const featuresData = [
  "Melhoria do Onboarding e Primeiros Passos(Peso: 5)",
  "Coleta e Utilização de Feedback do Usuário(Peso: 5)",
  "Aprimoramento da Navegação e Usabilidade da Plataforma(Peso: 4)",
  "Personalização e Acompanhamento de Estudos(Peso: 4)",
  "Aumento do Engajamento e Interação na Comunidade (Fórum)(Peso: 3)",
  "Implementação de Novas Funcionalidades e Recursos(Peso: 2)",
  "Aprimoramento da Interatividade nas Aulas(Peso: 2)",
  "Facilitar a Edição do Perfil do Usuário(Peso: 1)",
  "Incentivo à Avaliação da Plataforma(Peso: 1)",
  "Destaque de Avaliações Positivas(Peso: 1)",
  "Integração com Outras Ferramentas(Peso: 1)"
];

const ITEMS_PER_PAGE = 5;

export default function PrincipaisFeatures() {
  const [currentPage, setCurrentPage] = useState(0);

  const totalPages = Math.ceil(featuresData.length / ITEMS_PER_PAGE);
  const startIndex = currentPage * ITEMS_PER_PAGE;
  const paginatedData = featuresData.slice(startIndex, startIndex + ITEMS_PER_PAGE);

  const handlePrev = () => {
    if (currentPage > 0) setCurrentPage(prev => prev - 1);
  };

  const handleNext = () => {
    if (currentPage < totalPages - 1) setCurrentPage(prev => prev + 1);
  };

  return (
    <Stack spacing={4} width="full" height="100%">
      <Heading size="md">Principais Melhorias</Heading>

      <Box overflowY="auto" borderWidth="1px" borderRadius="md" minH="70%">
        <Table.Root variant="simple" width="100%">
          <Table.Header>
            <Table.Row>
              <Table.ColumnHeader>Descrição</Table.ColumnHeader>
            </Table.Row>
          </Table.Header>
          <Table.Body>
            {paginatedData.map((item, index) => (
              <Table.Row key={index}>
                <Table.Cell>{item}</Table.Cell>
              </Table.Row>
            ))}
          </Table.Body>
        </Table.Root>
      </Box>

      <Flex justify="space-between" align="center" w="full">
        <Stack direction="row" spacing={2}>
          <Button onClick={handlePrev} isDisabled={currentPage === 0}>
            Anterior
          </Button>
          <Button onClick={handleNext} isDisabled={currentPage >= totalPages - 1}>
            Próximo
          </Button>
        </Stack>
        <Text fontSize="sm" color="gray.600">
          Total: {featuresData.length}
        </Text>
      </Flex>
    </Stack>
  );
}
