import React from 'react';
import { BrowserRouter as Router } from 'react-router-dom';
import { ChakraProvider, extendTheme } from '@chakra-ui/react';
import Layout from './components/Layout';

const theme = extendTheme({
  config: {
    initialColorMode: 'light',
    useSystemColorMode: false,
  },
  colors: {
    brand: {
      50: '#E6FFFA',
      100: '#B2F5EA',
      200: '#81E6D9',
      300: '#4FD1C5',
      400: '#38B2AC',
      500: '#319795',
      600: '#2C7A7B',
      700: '#285E61',
      800: '#234E52',
      900: '#1D4044',
    },
    darkMode: {
      bg: '#0D1117',
      cardBg: '#161B22',
      borderColor: '#30363D',
      text: '#C9D1D9',
      subtext: '#8B949E',
    },
  },
  styles: {
    global: (props) => ({
      body: {
        bg: props.colorMode === 'light' ? 'gray.50' : 'darkMode.bg',
        color: props.colorMode === 'light' ? 'gray.900' : 'darkMode.text',
      },
    }),
  },
  components: {
    Button: {
      baseStyle: {
        fontWeight: 'medium',
        borderRadius: 'md',
      },
      variants: {
        solid: (props) => ({
          bg: props.colorScheme === "brand" ? "brand.500" : undefined,
          color: 'white',
          _hover: {
            bg: props.colorScheme === "brand" ? "brand.600" : undefined,
            _disabled: {
              bg: props.colorScheme === "brand" ? "brand.500" : undefined,
            },
          },
        }),
        ghost: (props) => ({
          color: props.colorMode === 'light' ? 'gray.600' : 'darkMode.subtext',
          _hover: {
            bg: props.colorMode === 'light' ? 'gray.100' : 'whiteAlpha.200',
          },
        }),
      },
    },
    Table: {
      baseStyle: (props) => ({
        th: {
          borderColor: props.colorMode === 'light' ? 'gray.200' : 'darkMode.borderColor',
          color: props.colorMode === 'light' ? 'gray.600' : 'darkMode.subtext',
          fontSize: 'sm',
        },
        td: {
          borderColor: props.colorMode === 'light' ? 'gray.200' : 'darkMode.borderColor',
        },
      }),
    },
  },
});

function App() {
  return (
    <ChakraProvider theme={theme}>
      <Router>
        <Layout />
      </Router>
    </ChakraProvider>
  );
}

export default App;

