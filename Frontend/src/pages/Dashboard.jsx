import { Box, Flex, VStack } from '@chakra-ui/react';
import Chart from '../components/Chart';
import FeedbacksTable from '../components/FeedbacksTable';
import Filtros from '../components/Filtros';
import PrincipaisFeatures from '../components/PrincipaisFeatures';
import { fetchWeeklyMetrics } from '@/services/apiService';
import { useEffect, useState } from 'react';

export default function Dashboard() {
  const [metrics, setMetrics] = useState(null);
  const [loading, setLoading] = useState(true);

  const today = new Date();
const sevenDaysAgo = new Date(today);
sevenDaysAgo.setDate(today.getDate() - 7);

const formatDate = (date) => {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
};

const startDate = formatDate(sevenDaysAgo);
const endDate = formatDate(today);

  const fetchMetricsData = async () => {
    try {
      const data = await fetchWeeklyMetrics(startDate, endDate);
      setMetrics(data);
      setLoading(false);
    } catch (error) {
      console.error('Erro ao carregar métricas:', error);
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchMetricsData();
  }, []);

  const handleFilter = () => {
    console.log('Filtros aplicados:', { startDate, endDate });
  };

  return (
    <Box
      width="100vw"
      minHeight="100vh"
      p={4}
      bg="gray.100"
      overflow="hidden"
      borderRadius="md"
    >
      <VStack spacing={4} height="100%">

      <Box w="full">
          <h1 style={{ fontSize: '24px', fontWeight: 'bold', alignContent: 'center', textAlign: 'center' }}>Dashboard Feedbacks - Alumind</h1>
        </Box>



        {/* Filtros */}
        <Box w="full">
          <Filtros 
            onFilter={handleFilter} 
            defaultStartDate={startDate} 
            defaultEndDate={endDate} 
          />
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
            {!loading && metrics ? <Chart data={metrics} /> : null}
          </Box>
          <Box
            flex="1"
            bg="white"
            p={4}
            borderRadius="md"
            shadow="md"
            overflow="hidden"
          >
            {!loading && metrics ? <PrincipaisFeatures data={metrics} /> : null}
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
