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
import { useEffect, useState } from "react";
import ModalDetails from "./ModalDetails";
import { fetchFeedbacks } from "@/services/apiService";

const PAGE_SIZE_LOCAL = 10;
const PAGE_SIZE = 100;

export default function FeedbacksTable() {
  const [loadedPages, setLoadedPages] = useState({});
  const [loading, setLoading] = useState(false);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalRecords, setTotalRecords] = useState(0);
  const [selectedFeedbackId, setSelectedFeedbackId] = useState(null);
  const [open, setOpen] = useState(false);

  const currentDataBlock = Math.floor((currentPage * PAGE_SIZE_LOCAL) / PAGE_SIZE);
  const allLoadedFeedbacks = Object.values(loadedPages).flat();
  const startIndex = currentPage * PAGE_SIZE_LOCAL;
  const paginatedFeedbacks = allLoadedFeedbacks.slice(startIndex, startIndex + PAGE_SIZE_LOCAL);

  const today = new Date();
  const sevenDaysAgo = new Date(today);
  sevenDaysAgo.setDate(today.getDate() - 7);

  const formatDate = (date) => {
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      return `${day}/${month}/${year}`;
  };

  useEffect(() => {
    const loadRemotePage = async () => {
      if (!loadedPages[currentDataBlock]) {
        setLoading(true);
        try {
          const params = {
            size: PAGE_SIZE,
            page: currentDataBlock,
            dtStart: formatDate(sevenDaysAgo),
            dtEnd: formatDate(today),
          };
          const data = await fetchFeedbacks(params);
          setLoadedPages((prev) => ({
            ...prev,
            [currentDataBlock]: data.feedbacks,
          }));
          setTotalRecords(data.infoPage.totalRecords);
        } catch (error) {
          console.error("Erro ao carregar feedbacks:", error);
        } finally {
          setLoading(false);
        }
      }
    };
  
    loadRemotePage();
  }, [currentDataBlock, loadedPages]);

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
                      isDisabled={
                          loading ||
                          (currentPage + 1) * PAGE_SIZE >= totalRecords
                      }
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