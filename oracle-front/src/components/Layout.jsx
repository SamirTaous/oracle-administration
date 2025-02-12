import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import {
  Box,
  Flex,
  VStack,
  Heading,
  Text,
  Icon,
  IconButton,
  Avatar,
  Menu,
  MenuButton,
  MenuList,
  MenuItem,
  useColorMode,
  useColorModeValue,
} from '@chakra-ui/react';
import {
  FiHome,
  FiUsers,
  FiDatabase,
  FiShield,
  FiBarChart2,
  FiZap,
  FiServer,
  FiSun,
  FiMoon,
} from 'react-icons/fi';
import Dashboard from './Dashboard';
import UserManagement from './UserManagement';
import BackupRestore from './BackupRestore';
import DataSecurity from './DataSecurity';
import PerformanceMonitoring from './PerformanceMonitoring';
import PerformanceOptimization from './PerformanceOptimization';
import HighAvailability from './HighAvailability';

const navItems = [
  { icon: FiHome, label: "Dashboard", href: "/" },
  { icon: FiUsers, label: "User Management", href: "/users" },
  { icon: FiDatabase, label: "Backup & Restore", href: "/backup" },
  { icon: FiShield, label: "Data Security", href: "/security" },
  { icon: FiBarChart2, label: "Performance Monitoring", href: "/performance" },
  { icon: FiZap, label: "Performance Optimization", href: "/optimization" },
  // { icon: FiServer, label: "High Availability", href: "/high-availability" },
];

function Layout() {
  const location = useLocation();
  const { colorMode, toggleColorMode } = useColorMode();

  // Color mode values
  const bg = useColorModeValue('gray.50', 'darkMode.bg');
  const sidebarBg = useColorModeValue('white', 'darkMode.cardBg');
  const borderColor = useColorModeValue('gray.200', 'darkMode.borderColor');
  const textColor = useColorModeValue('gray.900', 'darkMode.text');
  const menuItemBg = useColorModeValue('gray.50', 'darkMode.cardBg');
  const menuItemHoverBg = useColorModeValue('gray.100', 'whiteAlpha.200');
  const navItemColor = useColorModeValue('gray.600', 'darkMode.subtext');

  const getComponent = () => {
    switch (location.pathname) {
      case '/':
        return <Dashboard />;
      case '/users':
        return <UserManagement />;
      case '/backup':
        return <BackupRestore />;
      case '/security':
        return <DataSecurity />;
      case '/performance':
        return <PerformanceMonitoring />;
      case '/optimization':
        return <PerformanceOptimization />;
      // case '/high-availability':
      //   return <HighAvailability />;
      default:
        return <Dashboard />;
    }
  };

  return (
    <Flex minH="100vh" bg={bg}>
      {/* Sidebar */}
      <Box
        w="250px"
        bg={sidebarBg}
        borderRight="1px"
        borderColor={borderColor}
        position="fixed"
        h="100vh"
        py={5}
      >
        <VStack spacing={1} align="stretch">
          <Heading size="md" mb={6} px={4} color={textColor}>OracleAdmin</Heading>
          {navItems.map((item) => (
            <Link key={item.href} to={item.href}>
              <Flex
                align="center"
                px={4}
                py={3}
                mx={2}
                borderRadius="md"
                role="group"
                cursor="pointer"
                transition="all 0.2s"
                bg={location.pathname === item.href ? 'brand.500' : 'transparent'}
                color={location.pathname === item.href ? 'white' : navItemColor}
                _hover={{
                  bg: location.pathname === item.href ? 'brand.600' : menuItemHoverBg,
                  color: location.pathname === item.href ? 'white' : textColor,
                }}
              >
                <Icon as={item.icon} mr={3} boxSize={5} />
                <Text fontSize="sm" fontWeight="medium">{item.label}</Text>
              </Flex>
            </Link>
          ))}
        </VStack>
      </Box>

      {/* Main Content */}
      <Box ml="250px" w="calc(100% - 250px)" h="100vh">
        <Flex
          h="16"
          align="center"
          justify="space-between"
          px={8}
          borderBottom="1px"
          borderColor={borderColor}
          bg={sidebarBg}
        >
          <Heading size="lg" color={textColor}>
            {navItems.find(item => item.href === location.pathname)?.label}
          </Heading>
          <Flex align="center" gap={4}>
            <IconButton
              aria-label="Toggle color mode"
              icon={colorMode === 'light' ? <FiMoon /> : <FiSun />}
              onClick={toggleColorMode}
              variant="ghost"
              color={navItemColor}
            />
          </Flex>
        </Flex>
        <Box p={8} overflowY="auto" h="calc(100vh - 64px)">
          {getComponent()}
        </Box>
      </Box>
    </Flex>
  );
}

export default Layout;

