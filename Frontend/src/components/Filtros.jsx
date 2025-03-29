import {
  Box,
  Button,
  Flex,
  HStack,
  Input,
  VStack,
  Icon,
} from '@chakra-ui/react';
import { FiSearch, FiMail } from 'react-icons/fi';

const Filtros = ({ onFilter }) => {
  return (
    <Box bg="white" p={4} borderRadius="md" shadow="md" w="full">
      <VStack align="stretch" spacing={4}>
        <HStack spacing={4} align="stretch">
          <Input placeholder="Código" flex={1} />
          <Input placeholder="Selecione a data" type="date" flex={1} />
          <Input placeholder="Selecione a data" type="date" flex={1} />
          <Input placeholder="ID" flex={1} />
        </HStack>
        <Flex gap={4}>
          <Button leftIcon={<Icon as={FiSearch} />}   onClick={onFilter} colorScheme="blackAlpha">
            Buscar
          </Button>
          <Button leftIcon={<Icon as={FiMail} />} variant="outline">
            Enviar Relatório
          </Button>
        </Flex>
      </VStack>
    </Box>
  );
};

export default Filtros;
