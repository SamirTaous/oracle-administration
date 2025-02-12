import React, { useState } from 'react';
import {
  Box,
  Button,
  VStack,
  Heading,
  Text,
  SimpleGrid,
  Badge,
} from '@chakra-ui/react';

function HighAvailability() {
  const [dataGuardStatus, setDataGuardStatus] = useState({
    primaryDatabase: 'OraclePrimary',
    standbyDatabase: 'OracleStandby',
    status: 'Synchronized',
  });

  const handleSimulateFailover = () => {
    setDataGuardStatus(prevStatus => ({
      ...prevStatus,
      primaryDatabase: prevStatus.standbyDatabase,
      standbyDatabase: prevStatus.primaryDatabase,
      status: 'Failover Completed',
    }));
  };

  const handleSimulateSwitchover = () => {
    setDataGuardStatus(prevStatus => ({
      ...prevStatus,
      primaryDatabase: prevStatus.standbyDatabase,
      standbyDatabase: prevStatus.primaryDatabase,
      status: 'Switchover Completed',
    }));
  };

  return (
    <Box>
      <Heading mb={6}>High Availability</Heading>
      <VStack spacing={6} align="stretch">
        <Box p={5} shadow="md" borderWidth="1px" borderRadius="md">
          <Heading size="md" mb={4}>Oracle Data Guard Status</Heading>
          <SimpleGrid columns={2} spacing={4}>
            <Text fontWeight="bold">Primary Database:</Text>
            <Text>{dataGuardStatus.primaryDatabase}</Text>
            <Text fontWeight="bold">Standby Database:</Text>
            <Text>{dataGuardStatus.standbyDatabase}</Text>
            <Text fontWeight="bold">Status:</Text>
            <Badge colorScheme={dataGuardStatus.status === 'Synchronized' ? 'green' : 'orange'}>
              {dataGuardStatus.status}
            </Badge>
          </SimpleGrid>
        </Box>
        <SimpleGrid columns={2} spacing={4}>
          <Button colorScheme="blue" onClick={handleSimulateFailover}>
            Simulate Failover
          </Button>
          <Button colorScheme="green" onClick={handleSimulateSwitchover}>
            Simulate Switchover
          </Button>
        </SimpleGrid>
      </VStack>
    </Box>
  );
}

export default HighAvailability;

