import {
  Box,
  Button,
  Flex,
  HStack,
  Input,
  VStack,
  Icon,
  Portal, 
  Select, 
  createListCollection 
} from '@chakra-ui/react';
import { FiSearch } from 'react-icons/fi';
import { useState, useEffect } from 'react';

const options = createListCollection({
  items: [
    { label: "Positivo", value: "POSITIVO" },
    { label: "Negativo", value: "NEGATIVO" },
    { label: "Neutro", value: "NEUTRO" },
    { label: "Inconclusivo", value: "INCONCLUSIVO" },
  ],
});

const Filtros = ({ onFilter, defaultStartDate, defaultEndDate}) => {
  const [startDate, setStartDate] = useState(defaultStartDate);
  const [endDate, setEndDate] = useState(defaultEndDate);
  const [id, setId] = useState('');
  const [sentiment, setSentiment] = useState('');
  const [loading, setLoading] = useState(false)


  useEffect(() => {
    setStartDate(defaultStartDate);
    setEndDate(defaultEndDate);
  }, [defaultStartDate, defaultEndDate]);

  const handleApplyFilters = async () => {
    setLoading(!loading)
    await onFilter({ startDate, endDate, sentiment, id });
    setLoading(loading)
  };

  return (
    <Box bg="white" p={4} borderRadius="md" shadow="md" w="full">
      <VStack align="stretch" spacing={4}>
        <HStack spacing={4} align="stretch">
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
          <Select.Root
              collection={options}
              onChange={(e) => setSentiment(e.target.value)}
              width="300px"
            >
              <Select.HiddenSelect/>
              <Select.Control>
                <Select.Trigger>
                  <Select.ValueText placeholder="Selecione uma Opção" />
                </Select.Trigger>
                <Select.IndicatorGroup>
                  <Select.ClearTrigger />
                  <Select.Indicator />
                </Select.IndicatorGroup>
              </Select.Control>
              <Portal>
                <Select.Positioner>
                  <Select.Content>
                    {options.items.map((option) => (
                      <Select.Item item={option} key={option.value}>
                        {option.label}
                        <Select.ItemIndicator />
                      </Select.Item>
                    ))}
                  </Select.Content>
                </Select.Positioner>
              </Portal>
            </Select.Root>
        </HStack>
        <Flex gap={4}>
            <Button
              loading={loading} 
              onClick={handleApplyFilters}
              colorScheme="blackAlpha" 
              loadingText="Buscando..."
              spinnerPlacement="start"
            >
              Buscar
            </Button>
        </Flex>
      </VStack>
    </Box>
  );
};

export default Filtros;
