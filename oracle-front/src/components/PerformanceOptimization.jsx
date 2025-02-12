import React, { useState, useEffect } from 'react';
import {
  Box,
  Button,
  Table,
  Thead,
  Tbody,
  Tr,
  Th,
  Td,
  Heading,
  Text,
  VStack,
  HStack,
} from '@chakra-ui/react';

// Simulated slow query data
const fetchSlowQueriesFromDatabase = () => [
  {
    id: 1,
    sqlText: `SELECT * FROM orders o 
              JOIN customers c ON o.customer_id = c.customer_id 
              WHERE o.order_date > TO_DATE('2025-01-01', 'YYYY-MM-DD')`,
    executionTime: 850, // milliseconds
    rowsExamined: 5000,
  },
  {
    id: 2,
    sqlText: `SELECT product_id, COUNT(*) FROM sales 
              WHERE region = 'North' 
              GROUP BY product_id`,
    executionTime: 620,
    rowsExamined: 4000,
  },
  {
    id: 3,
    sqlText: `UPDATE inventory 
              SET stock = stock - ? 
              WHERE product_id = ?`,
    executionTime: 450,
    rowsExamined: 1000,
  },
];

function PerformanceOptimization() {
  const [slowQueries, setSlowQueries] = useState([]);
  const [lastOptimized, setLastOptimized] = useState(null);
  const [lastRecalculated, setLastRecalculated] = useState(null);

  // Simulate data fetching
  useEffect(() => {
    const data = fetchSlowQueriesFromDatabase();
    setSlowQueries(data);
  }, []);

  const handleOptimizeQuery = (id) => {
    setSlowQueries((queries) =>
      queries.map((query) =>
        query.id === id
          ? {
              ...query,
              executionTime: Math.floor(query.executionTime * 0.8), // Reduce by 20%
            }
          : query
      )
    );
    setLastOptimized(new Date().toLocaleString());
  };

  const handleRecalculateStats = () => {
    setLastRecalculated(new Date().toLocaleString());
  };

  return (
    <Box>
      <Heading mb={6}>Performance Optimization</Heading>
      <VStack spacing={6} align="stretch">
        <Box>
          <Heading size="md" mb={4}>Slow Queries</Heading>
          <Table variant="simple">
            <Thead>
              <Tr>
                <Th>Query ID</Th>
                <Th>SQL Text</Th>
                <Th>Execution Time</Th>
                <Th>Rows Examined</Th>
                <Th>Actions</Th>
              </Tr>
            </Thead>
            <Tbody>
              {slowQueries.map((query) => (
                <Tr key={query.id}>
                  <Td>{query.id}</Td>
                  <Td>
                    <Text isTruncated maxWidth="300px">{query.sqlText}</Text>
                  </Td>
                  <Td>{query.executionTime} ms</Td>
                  <Td>{query.rowsExamined}</Td>
                  <Td>
                    <Button
                      colorScheme="blue"
                      size="sm"
                      onClick={() => handleOptimizeQuery(query.id)}
                    >
                      Optimize
                    </Button>
                  </Td>
                </Tr>
              ))}
            </Tbody>
          </Table>
        </Box>
        <Box>
          <Heading size="md" mb={4}>Statistics Recalculation</Heading>
          <HStack spacing={4}>
            <Button colorScheme="green" onClick={handleRecalculateStats}>
              Recalculate Statistics
            </Button>
            {lastRecalculated && (
              <Text fontSize="sm" color="gray.600">
                Last recalculation: {lastRecalculated}
              </Text>
            )}
          </HStack>
        </Box>
        {lastOptimized && (
          <Text fontSize="sm" color="gray.600">
            Last query optimized: {lastOptimized}
          </Text>
        )}
      </VStack>
    </Box>
  );
}

export default PerformanceOptimization;
