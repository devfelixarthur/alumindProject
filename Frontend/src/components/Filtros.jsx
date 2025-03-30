import {
  Box,
  Button,
  Flex,
  HStack,
  Input,
  VStack,
  Icon,
} from '@chakra-ui/react';
import { FiSearch } from 'react-icons/fi';
import { useState, useEffect } from 'react';

const Filtros = ({ onFilter, defaultStartDate, defaultEndDate }) => {
  const [startDate, setStartDate] = useState(defaultStartDate);
  const [endDate, setEndDate] = useState(defaultEndDate);
  const [code, setCode] = useState('');
  const [id, setId] = useState('');

  useEffect(() => {
    // Atualiza as datas quando o componente receber novas props
    setStartDate(defaultStartDate);
    setEndDate(defaultEndDate);
  }, [defaultStartDate, defaultEndDate]);

  const handleApplyFilters = () => {
    onFilter({ startDate, endDate, code, id });
  };

  return (
    <Box bg="white" p={4} borderRadius="md" shadow="md" w="full">
      <VStack align="stretch" spacing={4}>
        <HStack spacing={4} align="stretch">
          <Input
            placeholder="Código"
            flex={1}
            value={code}
            onChange={(e) => setCode(e.target.value)}
          />
          <Input
            type="date"
            flex={1}
            value={startDate}
            onChange={(e) => setStartDate(e.target.value)}
          />
          <Input
            type="date"
            flex={1}
            value={endDate}
            onChange={(e) => setEndDate(e.target.value)}
          />
          <Input
            placeholder="ID"
            flex={1}
            value={id}
            onChange={(e) => setId(e.target.value)}
          />
        </HStack>
        <Flex gap={4}>
          <Button leftIcon={<Icon as={FiSearch} />} onClick={handleApplyFilters} colorScheme="blackAlpha">
            Buscar
          </Button>
        </Flex>
      </VStack>
    </Box>
  );
};

export default Filtros;
