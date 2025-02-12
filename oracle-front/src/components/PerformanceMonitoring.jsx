"use client"

import React, { useState, useEffect } from "react"
import {
  ChakraProvider,
  Box,
  Heading,
  Tabs,
  TabList,
  TabPanels,
  Tab,
  TabPanel,
  SimpleGrid,
  Stat,
  StatLabel,
  StatNumber,
  StatHelpText,
  useToast,
} from "@chakra-ui/react"
import { fetchAwrReport, fetchAshReport, fetchRealTimeStats } from "../api"
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from "recharts"

const PerformanceMonitoring = () => {
  const [awrData, setAwrData] = useState([])
  const [ashData, setAshData] = useState([])
  const [realTimeStats, setRealTimeStats] = useState({})
  const toast = useToast()

  useEffect(() => {
    const fetchData = async () => {
      try {
        const awr = await fetchAwrReport()
        setAwrData(awr)

        const ash = await fetchAshReport()
        setAshData(ash)

        const stats = await fetchRealTimeStats()
        setRealTimeStats(
          stats.reduce((acc, stat) => {
            acc[stat.metric_name] = Number.parseFloat(stat.value)
            return acc
          }, {}),
        )
      } catch (error) {
        console.error("Error fetching data:", error)
        toast({
          title: "Error fetching data",
          description: "Please try again later.",
          status: "error",
          duration: 5000,
          isClosable: true,
        })
      }
    }

    fetchData()
    const interval = setInterval(fetchData, 1000) // Refresh every minute

    return () => clearInterval(interval)
  }, [toast])

  const formatAwrData = (data) => {
    return data.map((item, index) => ({
      name: `Snap ${item.value}`,
      value: Number.parseInt(item.value),
    }))
  }

  const formatAshData = (data) => {
    return data.map((item, index) => ({
      name: `Sample ${index + 1}`,
      value: Number.parseInt(item.value),
    }))
  }

  return (
    <ChakraProvider>
      <Box maxWidth="1200px" margin="auto" p={4}>
        <Heading as="h1" size="xl" mb={4}>
          Database Performance Monitoring
        </Heading>

        <Tabs isFitted variant="enclosed">
          <TabList mb="1em">
            <Tab>Real-Time Stats</Tab>
            <Tab>AWR Report</Tab>
            <Tab>ASH Report</Tab>
          </TabList>
          <TabPanels>
            <TabPanel>
              <SimpleGrid columns={{ base: 1, md: 3 }} spacing={10}>
                <Stat>
                  <StatLabel>CPU Usage</StatLabel>
                  <StatNumber>{(realTimeStats.cpu_usage * 10).toFixed(2)}%</StatNumber>
                  <StatHelpText>Current CPU utilization</StatHelpText>
                </Stat>
                <Stat>
                  <StatLabel>I/O Throughput</StatLabel>
                  <StatNumber>{realTimeStats.io_throughput?.toFixed(2)} KB/s</StatNumber>
                  <StatHelpText>Current I/O performance</StatHelpText>
                </Stat>
                <Stat>
                  <StatLabel>Memory Usage</StatLabel>
                  <StatNumber>{realTimeStats.memory_usage?.toFixed(2)} MB</StatNumber>
                  <StatHelpText>Current memory consumption</StatHelpText>
                </Stat>
              </SimpleGrid>
            </TabPanel>
            <TabPanel>
              <Box height="400px">
                <ResponsiveContainer width="100%" height="100%">
                  <LineChart data={formatAwrData(awrData)}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="name" />
                    <YAxis />
                    <Tooltip />
                    <Legend />
                    <Line type="monotone" dataKey="value" stroke="#8884d8" name="Snap ID" />
                  </LineChart>
                </ResponsiveContainer>
              </Box>
            </TabPanel>
            <TabPanel>
              <Box height="400px">
                <ResponsiveContainer width="100%" height="100%">
                  <LineChart data={formatAshData(ashData)}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="name" />
                    <YAxis />
                    <Tooltip />
                    <Legend />
                    <Line type="monotone" dataKey="value" stroke="#82ca9d" name="Sample ID" />
                  </LineChart>
                </ResponsiveContainer>
              </Box>
            </TabPanel>
          </TabPanels>
        </Tabs>
      </Box>
    </ChakraProvider>
  )
}

export default PerformanceMonitoring

