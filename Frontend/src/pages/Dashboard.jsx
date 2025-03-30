import React, { useState, useEffect } from 'react';
import { Box, Flex, VStack } from '@chakra-ui/react';
import Chart from '../components/Chart';
import FeedbacksTable from '../components/FeedbacksTable';
import Filtros from '../components/Filtros';
import PrincipaisFeatures from '../components/PrincipaisFeatures';
import { fetchWeeklyMetrics, fetchFeedbacks } from '@/services/apiService';
import { validateDateFilters } from '@/utils/globalFunctions';

export default function Dashboard() {
  const [metrics, setMetrics] = useState(null);
  const [feedbacks, setFeedbacks] = useState([]);
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

  const fetchMetricsData = async (startDate, endDate) => {
    try {
      const dataMetrics = await fetchWeeklyMetrics(startDate, endDate);
      setMetrics(dataMetrics);
    } catch (error) {
      console.error('Erro ao carregar métricas:', error);
    }
  };

  const fetchFeedbacksData = async (params) => {
    try {
      const data = await fetchFeedbacks(params);
      setFeedbacks(data.feedbacks);
    } catch (error) {
      console.error('Erro ao carregar feedbacks:', error);
    }
  };

  useEffect(() => {
    handleFilter({ startDate, endDate});
  }, []);

  const handleFilter = async ({ startDate, endDate, sentiment, id }) => {
    if (!validateDateFilters(startDate, endDate, 30)) {
      return;
    }
    try {
      await fetchMetricsData(startDate, endDate);
      const params = {
        size: 100,
        page: 0,
        dtStart: startDate,
        dtEnd: endDate,
        sentiment,
        id,
      };
      await fetchFeedbacksData(params);
    } catch (error) {
      console.error('Erro ao aplicar filtros:', error);
    }
  };

  return (
    <Box width="100vw" minHeight="100vh" p={4} bg="gray.100" overflow="hidden" borderRadius="md">
      <VStack spacing={4} height="100%">
        <Box w="full">
          <h1 style={{ fontSize: '24px', fontWeight: 'bold', textAlign: 'center' }}>Dashboard Feedbacks - Alumind</h1>
        </Box>

        <Box w="full">
          <Filtros
            onFilter={handleFilter}
            defaultStartDate={startDate}
            defaultEndDate={endDate}
          />
        </Box>

        <Flex flex="1" w="full" gap={4} overflow="hidden">
          <Box flex="0 0 40%" bg="white" p={4} borderRadius="md" shadow="md" overflow="hidden">
            {metrics ? <Chart data={metrics} /> : null}
          </Box>
          <Box flex="1" bg="white" p={4} borderRadius="md" shadow="md" overflow="hidden">
            {metrics ? <PrincipaisFeatures data={metrics} /> : null}
          </Box>
        </Flex>

        <Box flex="0 0 35%" w="full" bg="white" p={4} borderRadius="md" shadow="md" overflow="hidden">
          <FeedbacksTable key={feedbacks.length} data={feedbacks} />
        </Box>
      </VStack>
    </Box>
  );
}
