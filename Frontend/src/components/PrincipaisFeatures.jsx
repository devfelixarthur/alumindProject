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

const ITEMS_PER_PAGE = 5;

export default function PrincipaisFeatures({ data }) {
  const featuresData = data?.principalFeatures || [];
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
