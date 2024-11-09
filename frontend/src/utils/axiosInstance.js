import axios from 'axios';

const axiosInstance = axios.create({
  // baseURL: 'http://13.228.120.122:8080', // replace with your API base URL
  baseURL: 'http://13.228.120.122:8080', // replace with your API base URL
  headers: {
    'Content-Type': 'application/json',
  },
});

// Function to set JWT token
export const setAuthToken = (token) => {
  if (token) {
    // Set the token in the Authorization header as a Bearer token
    axiosInstance.defaults.headers.common['Authorization'] = `Bearer ${token}`;
  } else {
    // Remove the Authorization header if no token is provided
    delete axiosInstance.defaults.headers.common['Authorization'];
  }
};

export default axiosInstance;
