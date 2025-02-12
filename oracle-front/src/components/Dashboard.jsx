import React, { useState, useEffect } from 'react';
import {
  Box,
  SimpleGrid,
  Stat,
  StatLabel,
  StatNumber,
  StatHelpText,
  StatArrow,
  Icon,
  Flex,
  Text,
  Table,
  Thead,
  Tbody,
  Tr,
  Th,
  Td,
  Progress,
  Button,
  useToast,
  useColorModeValue,
} from '@chakra-ui/react';
import { FiUsers, FiDatabase, FiShield, FiCpu } from 'react-icons/fi';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import { fetchUsers } from '../api'; // Make sure to import fetchUsers

function Dashboard() {
  const [performanceData, setPerformanceData] = useState([]);
  const [alerts, setAlerts] = useState(1);
  const [userCount, setUserCount] = useState(0); // Add state for user count
  const toast = useToast();

  const cardBg = useColorModeValue('white', 'darkMode.cardBg');
  const borderColor = useColorModeValue('gray.200', 'darkMode.borderColor');
  const textColor = useColorModeValue('gray.600', 'darkMode.subtext');
  const statBg = useColorModeValue('gray.50', 'whiteAlpha.100');

  useEffect(() => {
    // Fetch the user count when the component mounts
    const getUserCount = async () => {
      try {
        const users = await fetchUsers();
        setUserCount(users.length); // Assuming users.length is the count
      } catch (error) {
        console.error("Error fetching users:", error);
      }
    };

    getUserCount();

    const generateData = () => {
      const now = new Date();
      const newData = {
        time: now.toLocaleTimeString(),
        cpu: Math.floor(Math.random() * 20) + 10,
        memory: Math.floor(Math.random() * 30) + 40,
        diskIO: Math.floor(Math.random() * 10) + 5,
      };
      setPerformanceData(prevData => [...prevData.slice(-19), newData]);
    };

    generateData();
    const interval = setInterval(generateData, 2000);
    return () => clearInterval(interval);
  }, []); // Empty dependency array to run on mount

  const handleClearAlerts = () => {
    setAlerts(0);
    toast({
      title: "Alerts cleared",
      description: "All security alerts have been acknowledged.",
      status: "success",
      duration: 3000,
      isClosable: true,
    });
  };

  return (
    <Box>
      <SimpleGrid columns={{ base: 1, md: 2, lg: 4 }} spacing={6} mb={8}>
        <Stat
          px={{ base: 2, md: 4 }}
          py={'5'}
          shadow={'xl'}
          border={'1px solid'}
          borderColor={borderColor}
          rounded={'lg'}
          bg={cardBg}
        >
          <Flex justifyContent={'space-between'}>
            <Box pl={{ base: 2, md: 4 }}>
              <StatLabel fontWeight={'medium'} isTruncated color={textColor}>
                Total Users
              </StatLabel>
              <StatNumber fontSize={'2xl'} fontWeight={'medium'}>
                {userCount} {/* Use dynamic user count */}
              </StatNumber>
              <StatHelpText>
                All active users
              </StatHelpText>
            </Box>
            <Box
              my={'auto'}
              color={'brand.500'}
              alignContent={'center'}
            >
              <Icon as={FiUsers} w={10} h={10} />
            </Box>
          </Flex>
        </Stat>

        <Stat
          px={{ base: 2, md: 4 }}
          py={'5'}
          shadow={'xl'}
          border={'1px solid'}
          borderColor={borderColor}
          rounded={'lg'}
          bg={cardBg}
        >
          <Flex justifyContent={'space-between'}>
            <Box pl={{ base: 2, md: 4 }}>
              <StatLabel fontWeight={'medium'} isTruncated color={textColor}>
                Last Backup
              </StatLabel>
              <StatNumber fontSize={'2xl'} fontWeight={'medium'}>
                12 minutes ago
              </StatNumber>
              <StatHelpText>
                Incremental backup completed
              </StatHelpText>
            </Box>
            <Box
              my={'auto'}
              color={'brand.500'}
              alignContent={'center'}
            >
              <Icon as={FiDatabase} w={10} h={10} />
            </Box>
          </Flex>
        </Stat>

        <Stat
          px={{ base: 2, md: 4 }}
          py={'5'}
          shadow={'xl'}
          border={'1px solid'}
          borderColor={borderColor}
          rounded={'lg'}
          bg={cardBg}
        >
          <Flex justifyContent={'space-between'}>
            <Box pl={{ base: 2, md: 4 }}>
              <StatLabel fontWeight={'medium'} isTruncated color={textColor}>
                Security Alerts
              </StatLabel>
              <StatNumber fontSize={'2xl'} fontWeight={'medium'}>
                {alerts}
              </StatNumber>
              <StatHelpText>
                <Button size="xs" colorScheme="red" onClick={handleClearAlerts}>
                  Clear Alerts
                </Button>
              </StatHelpText>
            </Box>
            <Box
              my={'auto'}
              color={'brand.500'}
              alignContent={'center'}
            >
              <Icon as={FiShield} w={10} h={10} />
            </Box>
          </Flex>
        </Stat>

        <Stat
          px={{ base: 2, md: 4 }}
          py={'5'}
          shadow={'xl'}
          border={'1px solid'}
          borderColor={borderColor}
          rounded={'lg'}
          bg={cardBg}
        >
          <Flex justifyContent={'space-between'}>
            <Box pl={{ base: 2, md: 4 }}>
              <StatLabel fontWeight={'medium'} isTruncated color={textColor}>
                CPU Usage
              </StatLabel>
              <StatNumber fontSize={'2xl'} fontWeight={'medium'}>
                {performanceData.length > 0 ? `${performanceData[performanceData.length - 1].cpu}%` : 'N/A'}
              </StatNumber>
              <StatHelpText>
                <StatArrow type="decrease" />
                1% since last hour
              </StatHelpText>
            </Box>
            <Box
              my={'auto'}
              color={'brand.500'}
              alignContent={'center'}
            >
              <Icon as={FiCpu} w={10} h={10} />
            </Box>
          </Flex>
        </Stat>
      </SimpleGrid>

      <SimpleGrid columns={{ base: 1, lg: 2 }} spacing={6}>
      <Box
  bg={cardBg}
  shadow={'xl'}
  border={'1px solid'}
  borderColor={borderColor}
  rounded={'lg'}
  p={6}
>
  <Text fontSize={'lg'} fontWeight={'medium'} mb={4} color={textColor}>
    Backup History
  </Text>
  <Table variant="simple">
    <Thead>
      <Tr>
        <Th>Backup Name</Th>
        <Th>Date & Time</Th>
        <Th>Type</Th>
      </Tr>
    </Thead>
    <Tbody>
      <Tr>
        <Td>backup_2025_01_10_12_30</Td>
        <Td>2025-01-10 12:30 PM</Td>
        <Td>Incremental</Td>
      </Tr>
      <Tr>
        <Td>backup_2025_01_09_18_00</Td>
        <Td>2025-01-09 06:00 PM</Td>
        <Td>Full</Td>
      </Tr>
    </Tbody>
  </Table>
</Box>


        <Box
          bg={cardBg}
          shadow={'xl'}
          border={'1px solid'}
          borderColor={borderColor}
          rounded={'lg'}
          p={6}
        >
          <Text fontSize={'lg'} fontWeight={'medium'} mb={4} color={textColor}>
            Performance Over Time
          </Text>
          <Box height="300px">
            <ResponsiveContainer width="100%" height="100%">
              <LineChart data={performanceData}>
                <CartesianGrid strokeDasharray="3 3" stroke={borderColor} />
                <XAxis dataKey="time" stroke={textColor} />
                <YAxis stroke={textColor} />
                <Tooltip contentStyle={{ backgroundColor: cardBg, borderColor: borderColor }} />
                <Legend />
                <Line type="monotone" dataKey="cpu" stroke="#8884d8" name="CPU Usage" />
                <Line type="monotone" dataKey="memory" stroke="#82ca9d" name="Memory Usage" />
                <Line type="monotone" dataKey="diskIO" stroke="#ffc658" name="Disk I/O" />
              </LineChart>
            </ResponsiveContainer>
          </Box>
        </Box>
      </SimpleGrid>
    </Box>
  );
}

export default Dashboard;
