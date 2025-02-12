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
  Input,
  Select,
  FormControl,
  FormLabel,
  VStack,
  HStack,
  Heading,
  Text,
  Progress,
  useToast,
} from '@chakra-ui/react';

const initialBackups = [
  { id: 1, type: 'full', date: '2024-12-28 14:30', status: 'Completed', size: '1000MB' },
  { id: 2, type: 'incremental', date: '2024-12-31 10:15', status: 'Completed', size: '100MB' }
];

function BackupRestore() {
  const [backups, setBackups] = useState(initialBackups);
  const [newBackup, setNewBackup] = useState({ type: 'full', schedule: '' });
  const [backupProgress, setBackupProgress] = useState(0);
  const [isBackingUp, setIsBackingUp] = useState(false);
  const toast = useToast();

  useEffect(() => {
    let interval;
    if (isBackingUp) {
      interval = setInterval(() => {
        setBackupProgress((prevProgress) => {
          if (prevProgress >= 100) {
            clearInterval(interval);
            setIsBackingUp(false);
            return 0;
          }
          return prevProgress + 10;
        });
      }, 1000);
    }
    return () => clearInterval(interval);
  }, [isBackingUp]);

  const handleCreateBackup = (e) => {
    e.preventDefault();
    setIsBackingUp(true);
    const id = backups.length + 1;
    const date = new Date().toLocaleString();
    const newBackupEntry = { id, ...newBackup, date, status: 'In Progress', size: 'Calculating...' };
    setBackups([newBackupEntry, ...backups]);
    setNewBackup({ type: 'full', schedule: '' });

    // Simulate backup completion
    setTimeout(() => {
      setBackups(prevBackups => 
        prevBackups.map(backup => 
          backup.id === id ? { ...backup, status: 'Completed', size: `${Math.floor(Math.random() * 500 + 100)}MB` } : backup
        )
      );
      toast({
        title: "Backup completed",
        description: "Your database backup has been successfully created.",
        status: "success",
        duration: 3000,
        isClosable: true,
      });
    }, 10000);
  };

  const handleRestoreBackup = (id) => {
    setBackups(backups.map(backup => 
      backup.id === id ? { ...backup, status: 'Restoring' } : backup
    ));
    
    // Simulate restore process
    setTimeout(() => {
      setBackups(prevBackups => 
        prevBackups.map(backup => 
          backup.id === id ? { ...backup, status: 'Completed' } : backup
        )
      );
      toast({
        title: "Restore completed",
        description: "Your database has been successfully restored.",
        status: "success",
        duration: 3000,
        isClosable: true,
      });
    }, 5000);
  };

  return (
    <Box>
      <Heading mb={6}>Backup and Restore</Heading>
      <VStack spacing={6} align="stretch">
        <Box>
          <form onSubmit={handleCreateBackup}>
            <HStack spacing={4}>
              <FormControl>
                <FormLabel>Backup Type</FormLabel>
                <Select
                  value={newBackup.type}
                  onChange={(e) => setNewBackup({ ...newBackup, type: e.target.value })}
                >
                  <option value="full">Full Backup</option>
                  <option value="incremental">Incremental Backup</option>
                </Select>
              </FormControl>
              <FormControl>
                <FormLabel>Schedule (cron format)</FormLabel>
                <Input
                  placeholder="0 2 * * *"
                  value={newBackup.schedule}
                  onChange={(e) => setNewBackup({ ...newBackup, schedule: e.target.value })}
                />
              </FormControl>
              <Button 
                type="submit" 
                colorScheme="brand" 
                mt={8} 
                isLoading={isBackingUp} 
                width="30%" 
              >
                Create Backup
              </Button>
            </HStack>
          </form>
        </Box>
        {isBackingUp && (
          <Box>
            <Text mb={2}>Backup in progress...</Text>
            <Progress value={backupProgress} size="sm" colorScheme="brand" />
          </Box>
        )}
        <Table variant="simple">
          <Thead>
            <Tr>
              <Th>ID</Th>
              <Th>Type</Th>
              <Th>Date</Th>
              <Th>Size</Th>
              <Th>Status</Th>
              <Th>Actions</Th>
            </Tr>
          </Thead>
          <Tbody>
            {backups.map((backup) => (
              <Tr key={backup.id}>
                <Td>{backup.id}</Td>
                <Td>{backup.type}</Td>
                <Td>{backup.date}</Td>
                <Td>{backup.size}</Td>
                <Td>{backup.status}</Td>
                <Td>
                  <Button
                    colorScheme="brand"
                    size="sm"
                    onClick={() => handleRestoreBackup(backup.id)}
                    isDisabled={backup.status === 'Restoring' || backup.status === 'In Progress'}
                  >
                    Restore
                  </Button>
                </Td>
              </Tr>
            ))}
          </Tbody>
        </Table>
      </VStack>
    </Box>
  );
}

export default BackupRestore;
