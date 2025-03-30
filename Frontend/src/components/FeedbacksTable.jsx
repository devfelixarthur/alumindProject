import {
  Box,
  Heading,
  Stack,
  Table,
  Button,
  Flex,
  Text,
  Spinner
} from "@chakra-ui/react";
import { useState } from "react";
import ModalDetails from "./ModalDetails";

const PAGE_SIZE_LOCAL = 10;
const PAGE_SIZE = 100;

export default function FeedbacksTable({ data }) {
  const [loading] = useState(false);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalRecords] = useState(data?.length || 0);
  const [selectedFeedbackId, setSelectedFeedbackId] = useState(null);
  const [open, setOpen] = useState(false);

  const startIndex = currentPage * PAGE_SIZE_LOCAL;
  const paginatedFeedbacks = data.slice(startIndex, startIndex + PAGE_SIZE_LOCAL);

  const handleNext = () => {
    if ((currentPage + 1) * PAGE_SIZE_LOCAL < totalRecords) {
      setCurrentPage((prev) => prev + 1);
    }
  };
  
  const handlePrev = () => { 
    if (currentPage > 0) {
      setCurrentPage((prev) => prev - 1);
    }
  };

  const handleViewDetails = async (id) => {
    setSelectedFeedbackId(id);
    setOpen(true);
  };

  const handleCloseModal = () => {
    setOpen(false);
  };

  return (
    <Stack spacing={4} width="full" h="100%">
      <Heading size="md">Lista de Feedbacks</Heading>

      <Box
        overflowY="auto"
        borderWidth="1px"
        borderRadius="md"
        flex="1"
        minH="75%"
      >
        <Table.Root variant="simple" width="full" minH="100%">
          <Table.Header>
            <Table.Row>
              <Table.ColumnHeader>Id</Table.ColumnHeader>
              <Table.ColumnHeader>Feedback</Table.ColumnHeader>
              <Table.ColumnHeader>Data</Table.ColumnHeader>
              <Table.ColumnHeader>Sentimento</Table.ColumnHeader>
              <Table.ColumnHeader>Ações</Table.ColumnHeader>
            </Table.Row>
          </Table.Header>
          <Table.Body>
            {loading ? (
              <Table.Row>
                <Table.Cell colSpan={3}>
                  <Flex justify="center">
                    <Spinner />
                  </Flex>
                </Table.Cell>
              </Table.Row>
            ) : (
              paginatedFeedbacks.map((item) => (
                <Table.Row key={item.id}>
                  <Table.Cell>{item.id}</Table.Cell>
                  <Table.Cell>{item.originalFeedback}</Table.Cell>
                  <Table.Cell>{item.dtRegister}</Table.Cell>
                  <Table.Cell>{item.sentiment}</Table.Cell>
                  <Table.Cell>
                    <Button
                      aria-label="Ver"
                      onClick={() => handleViewDetails(item.id)}
                      size="xs"
                    >
                      Ver
                    </Button>
                  </Table.Cell>
                </Table.Row>
              ))
            )}
          </Table.Body>
        </Table.Root>
      </Box>

      <Flex justify="space-between" align="center" w="full">
        <Stack direction="row" spacing={4}>
          <Button onClick={handlePrev} isDisabled={currentPage === 0}>
            Anterior
          </Button>
          <Button
            onClick={handleNext}
            isDisabled={loading || (currentPage + 1) * PAGE_SIZE >= totalRecords}
          >
            Próximo
          </Button>
        </Stack>
        <Text fontSize="sm" color="gray.600">
          Total: {totalRecords}
        </Text>
      </Flex>

      <ModalDetails
        id={selectedFeedbackId}
        onClose={handleCloseModal}
        open={open}
      />
    </Stack>
  );
}
