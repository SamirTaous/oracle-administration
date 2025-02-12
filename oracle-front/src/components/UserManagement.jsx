import React, { useState, useEffect } from 'react';
import {
  Box, Button, Table, Thead, Tbody, Tr, Th, Td, Input, Select, Modal,
  ModalOverlay, ModalContent, ModalHeader, ModalFooter, ModalBody,
  ModalCloseButton, useDisclosure, FormControl, FormLabel, useToast,
  IconButton, Flex,
} from '@chakra-ui/react';
import { FiTrash2 } from 'react-icons/fi';
import { fetchUsers, createUser, deleteUser } from '../api';

function UserManagement() {
  const [users, setUsers] = useState([]);
  const [newUser, setNewUser] = useState({ username: '', password: '', role: '', quota: '', tablespace: '' });
  const { isOpen, onOpen, onClose } = useDisclosure();
  const toast = useToast();

  useEffect(() => {
    const loadUsers = async () => {
      const data = await fetchUsers();
      setUsers(data);
    };
    loadUsers();
  }, []);

  const handleCreateUser = async () => {
    try {
      const response = await createUser(
        newUser.username,
        newUser.password,
        newUser.role === 'ADMIN_ROLE' ? 'ADMIN_ROLE' : newUser.role === 'USER_ROLE' ? 'USER_ROLE' : 'POWER_USER_ROLE',
        newUser.quota,
        newUser.tablespace === 'users' ? 'users' : 'USERS' // Ensure tablespace is correctly mapped
      );
      toast({
        title: "User created.",
        description: response,
        status: "success",
        duration: 3000,
        isClosable: true,
      });
      setNewUser({ username: '', password: '', role: '', quota: '', tablespace: '' });
      onClose();
      const data = await fetchUsers();
      setUsers(data);
    } catch (error) {
      toast({
        title: "Error creating user.",
        description: JSON.stringify(error.response?.data) || error.message || "An unknown error occurred.",
        status: "error",
        duration: 3000,
        isClosable: true,
      });
    }
  };
  

  const handleDeleteUser = async (username) => {
    try {
      await deleteUser(username);
      toast({
        title: "User deleted.",
        description: "User has been successfully removed.",
        status: "warning",
        duration: 3000,
        isClosable: true,
      });
      const data = await fetchUsers();
      setUsers(data);
    } catch (error) {
      toast({
        title: "Error deleting user.",
        description: JSON.stringify(error.response?.data) || error.message || "An unknown error occurred.",
        status: "error",
        duration: 3000,
        isClosable: true,
      });
    }
  };

  return (
    <Box>
      <Flex justifyContent="space-between" alignItems="center" mb={4}>
        <Button colorScheme="brand" onClick={onOpen}>
          Add New User
        </Button>
      </Flex>

      <Table variant="simple">
        <Thead>
          <Tr>
            <Th>Username</Th>
            <Th>Account Status</Th>
            <Th>Default Tablespace</Th>
            <Th>Temporary Tablespace</Th>
            <Th>Created</Th>
            <Th>Actions</Th>
          </Tr>
        </Thead>
        <Tbody>
          {users.map((user) => (
            <Tr key={user.USERNAME}>
              <Td>{user.USERNAME}</Td>
              <Td>{user.ACCOUNT_STATUS}</Td>
              <Td>{user.DEFAULT_TABLESPACE}</Td>
              <Td>{user.TEMPORARY_TABLESPACE}</Td>
              <Td>{user.CREATED}</Td>
              <Td>
                <IconButton
                  aria-label="Delete user"
                  icon={<FiTrash2 />}
                  size="sm"
                  colorScheme="red"
                  onClick={() => handleDeleteUser(user.USERNAME)}
                />
              </Td>
            </Tr>
          ))}
        </Tbody>
      </Table>

      <Modal isOpen={isOpen} onClose={onClose}>
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>Create New User</ModalHeader>
          <ModalCloseButton />
          <ModalBody>
            <FormControl>
              <FormLabel>Username</FormLabel>
              <Input
                value={newUser.username}
                onChange={(e) => setNewUser({ ...newUser, username: e.target.value })}
              />
            </FormControl>
            <FormControl mt={4}>
              <FormLabel>Password</FormLabel>
              <Input
                value={newUser.password}
                onChange={(e) => setNewUser({ ...newUser, password: e.target.value })}
              />
            </FormControl>
            <FormControl mt={4}>
              <FormLabel>Role</FormLabel>
              <Select
                value={newUser.role}
                onChange={(e) => setNewUser({ ...newUser, role: e.target.value })}
              >
                <option value="ADMIN_ROLE">Administrator</option>
                <option value="USER_ROLE">Regular User</option>
                <option value="POWER_USER_ROLE">Power User</option>
              </Select>
            </FormControl>
            <FormControl mt={4}>
              <FormLabel>Quota</FormLabel>
              <Input
                value={newUser.quota}
                onChange={(e) => setNewUser({ ...newUser, quota: e.target.value })}
              />
            </FormControl>
            <FormControl mt={4}>
              <FormLabel>Tablespace</FormLabel>
              <Input
                value={newUser.tablespace}
                onChange={(e) => setNewUser({ ...newUser, tablespace: e.target.value })}
              />
            </FormControl>
          </ModalBody>
          <ModalFooter>
            <Button colorScheme="brand" mr={3} onClick={handleCreateUser}>
              Create
            </Button>
            <Button variant="ghost" onClick={onClose}>
              Cancel
            </Button>
          </ModalFooter>
        </ModalContent>
      </Modal>
    </Box>
  );
}

export default UserManagement;
