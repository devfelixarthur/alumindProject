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

// 🧪 MOCK API SIMULADA
const mockLoadData = (page = 0) => {
  const feedbacks = Array.from({ length: 100 }, (_, i) => {
    const id = page * 100 + i + 1;
    return {
      id,
      feedback: `Tenho usado o Alumind há algum tempo. Ele funciona bem, mas não me empolga muito.`,
      data: `2025-03-${String((id % 30) + 1).padStart(2, "0")}`,
      sentiment: ["Positivo", "Negativo", "Neutro"][id % 3],
    };
  });

  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        infoPage: {
          pageNumber: page,
          pageSize: 100,
          totalRecords: 1000,
          totalPages: 10,
          hasPreviousPage: page > 0,
          hasNextPage: page < 9,
        },
        feedbacks,
      });
    }, 500);
  });
};


const PAGE_SIZE_LOCAL = 10;

export default function FeedbacksTable() {
  const [loadedPages, setLoadedPages] = useState({});
  const [currentPageIndex, setCurrentPageIndex] = useState(0);
  const [loading, setLoading] = useState(false);
  const [totalRecords, setTotalRecords] = useState(0);
  const [selectedFeedbackId, setSelectedFeedbackId] = useState(null);
  const [open, setOpen] = useState(false);


  const currentDataBlock = Math.floor((currentPageIndex * PAGE_SIZE_LOCAL) / 100);

  useEffect(() => {
    if (!loadedPages[currentDataBlock]) {
      setLoading(true);
      mockLoadData(currentDataBlock).then((data) => {
        setLoadedPages((prev) => ({
          ...prev,
          [currentDataBlock]: data.feedbacks,
        }));
        setTotalRecords(data.infoPage.totalRecords);
        setLoading(false);
      });
    }
  }, [currentDataBlock, loadedPages]);

  const allLoadedFeedbacks = Object.values(loadedPages).flat();
  const totalLoaded = allLoadedFeedbacks.length;

  const startIndex = currentPageIndex * PAGE_SIZE_LOCAL;
  const paginatedFeedbacks = allLoadedFeedbacks.slice(
    startIndex,
    startIndex + PAGE_SIZE_LOCAL
  );

  const handleNext = () => {
    setCurrentPageIndex((prev) => prev + 1);
  };

  const handlePrev = () => {
    setCurrentPageIndex((prev) => Math.max(0, prev - 1));
  };

  const handleViewDetails = async (id) => {
    setLoading(true);
    setSelectedFeedbackId(id);
    setLoading(false);
    setOpen(true)
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
                  <Table.Cell>{item.feedback}</Table.Cell>
                  <Table.Cell>{item.data}</Table.Cell>
                  <Table.Cell>{item.sentiment}</Table.Cell>
                  <Table.Cell>
                    <Button 
                    aria-label="Ver"
                    onClick={() => handleViewDetails(item.id)}
                    size="xs">Ver
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
          <Button onClick={handlePrev} isDisabled={currentPageIndex === 0}>
            Anterior
          </Button>
          <Button
            onClick={handleNext}
            isDisabled={
              loading ||
              (startIndex + PAGE_SIZE_LOCAL >= totalLoaded &&
                !loadedPages[currentDataBlock + 1])
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