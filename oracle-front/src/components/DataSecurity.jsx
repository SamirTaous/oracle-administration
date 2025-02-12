import React, { useState, useEffect } from "react"
import {
  Box,
  VStack,
  Heading,
  Text,
  Select,
  Button,
  useToast,
  Alert,
  AlertIcon,
  AlertTitle,
  AlertDescription,
  Table,
  Thead,
  Tbody,
  Tr,
  Th,
  Td,
  TableContainer,
} from "@chakra-ui/react"
import { setAuditTrail, getAuditTrailValue, getTablespaceEncryption } from "../api"

function AuditTrailManager() {
  const [selectedAuditType, setSelectedAuditType] = useState("")
  const [currentAuditValue, setCurrentAuditValue] = useState("")
  const [isLoading, setIsLoading] = useState(false)
  const [showRestartPrompt, setShowRestartPrompt] = useState(false)
  const [tablespaceEncryption, setTablespaceEncryption] = useState([])
  const toast = useToast()

  useEffect(() => {
    fetchCurrentAuditValue()
    fetchTablespaceEncryption()
  }, [])

  const fetchCurrentAuditValue = async () => {
    setIsLoading(true)
    try {
      const value = await getAuditTrailValue()
      setCurrentAuditValue(value)
    } catch (error) {
      toast({
        title: "Error fetching audit trail value",
        description: error.message,
        status: "error",
        duration: 5000,
        isClosable: true,
      })
    } finally {
      setIsLoading(false)
    }
  }

  const fetchTablespaceEncryption = async () => {
    try {
      const data = await getTablespaceEncryption()
      setTablespaceEncryption(data)
    } catch (error) {
      toast({
        title: "Error fetching tablespace encryption data",
        description: error.message,
        status: "error",
        duration: 5000,
        isClosable: true,
      })
    }
  }

  const handleAuditTypeChange = (event) => {
    setSelectedAuditType(event.target.value)
  }

  const handleSetAuditTrail = async () => {
    if (!selectedAuditType) {
      toast({
        title: "No audit type selected",
        description: "Please select an audit type before setting the audit trail.",
        status: "warning",
        duration: 5000,
        isClosable: true,
      })
      return
    }

    setIsLoading(true)
    try {
      const result = await setAuditTrail(selectedAuditType)
      toast({
        title: "Audit trail set successfully",
        description: `Audit trail has been set to ${selectedAuditType}. Server response: ${result}`,
        status: "success",
        duration: 5000,
        isClosable: true,
      })
      setShowRestartPrompt(true)
      fetchCurrentAuditValue() // Refresh the current value after setting
    } catch (error) {
      toast({
        title: "Error setting audit trail",
        description: error.message,
        status: "error",
        duration: 5000,
        isClosable: true,
      })
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <Box maxWidth="800px" margin="auto" p={4}>
      <VStack spacing={6} align="stretch">
        <Heading as="h1" size="xl">
          Audit Trail Manager
        </Heading>

        <Box>
          <Text mb={2}>Select Audit Type:</Text>
          <Select placeholder="Select audit type" value={selectedAuditType} onChange={handleAuditTypeChange}>
            <option value="NONE">NONE</option>
            <option value="DB">DB</option>
            <option value="XML">XML</option>
            <option value="OS">OS</option>
          </Select>
        </Box>

        <Button
          colorScheme="blue"
          onClick={handleSetAuditTrail}
          isLoading={isLoading}
          loadingText="Setting audit trail..."
        >
          Set Audit Trail
        </Button>

        {showRestartPrompt && (
          <Alert status="warning">
            <AlertIcon />
            <AlertTitle mr={2}>Restart Required</AlertTitle>
            <AlertDescription>Please restart your container for the changes to take effect.</AlertDescription>
          </Alert>
        )}

        <Box>
          <Text mb={2}>Current Audit Trail Value:</Text>
          <Text fontWeight="bold">{currentAuditValue || "Loading..."}</Text>
        </Box>

        <Button
          colorScheme="green"
          onClick={fetchCurrentAuditValue}
          isLoading={isLoading}
          loadingText="Fetching current value..."
        >
          Refresh Current Value
        </Button>

        <Heading as="h2" size="lg" mt={6}>
          Tablespace Encryption
        </Heading>

        <TableContainer>
          <Table variant="simple">
            <Thead>
              <Tr>
                <Th>Tablespace Name</Th>
                <Th>Encrypted</Th>
              </Tr>
            </Thead>
            <Tbody>
              {tablespaceEncryption.map((item) => (
                <Tr key={item.TABLESPACE_NAME}>
                  <Td>{item.TABLESPACE_NAME}</Td>
                  <Td>{item.ENCRYPTED}</Td>
                </Tr>
              ))}
            </Tbody>
          </Table>
        </TableContainer>

        <Button colorScheme="teal" onClick={fetchTablespaceEncryption} mt={4}>
          Refresh Tablespace Encryption Data
        </Button>
      </VStack>
    </Box>
  )
}

export default AuditTrailManager

