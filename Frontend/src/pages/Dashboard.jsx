import { Box, Flex, VStack } from '@chakra-ui/react';
import Chart from '../components/Chart';
import FeedbacksTable from '../components/FeedbacksTable';
import Filtros from '../components/Filtros';
import PrincipaisFeatures from '../components/PrincipaisFeatures';

export default function Dashboard() {
  const handleFilter = () => {
    console.log('Filtros aplicados');
  };

  return (
    <Box
      width="100vw"
      height="100vh"
      p={4}
      bg="gray.100"
      overflow="hidden"
      borderRadius="md"
    >
      <VStack spacing={4} height="100%">
        {/* Filtros */}
        <Box w="full">
          <Filtros onFilter={handleFilter} />
        </Box>

        {/* Chart + Features lado a lado */}
        <Flex flex="1" w="full" gap={4} overflow="hidden">
          <Box
            flex="0 0 40%"
            bg="white"
            p={4}
            borderRadius="md"
            shadow="md"
            overflow="hidden"
          >
            <Chart />
          </Box>
          <Box
            flex="1"
            bg="white"
            p={4}
            borderRadius="md"
            shadow="md"
            overflow="hidden"
          >
            <PrincipaisFeatures />
          </Box>
        </Flex>

        {/* Tabela Feedbacks */}
        <Box
          flex="0 0 35%"
          w="full"
          bg="white"
          p={4}
          borderRadius="md"
          shadow="md"
          overflow="hidden"
        >
          <FeedbacksTable />
        </Box>
      </VStack>
    </Box>
  );
}
