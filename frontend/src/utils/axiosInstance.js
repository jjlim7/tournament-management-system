import axios from 'axios';
import Cookies from 'js-cookie';

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
    // Store the token in a cookie
    Cookies.set('authToken', token, { expires: 7, secure: true }); // expires in 7 days
    // Set the token in the Authorization header as a Bearer token
    axiosInstance.defaults.headers.common['Authorization'] = `Bearer ${token}`;
  } else {
    // Remove the token from the cookies
    Cookies.remove('authToken');
    // Remove the Authorization header
    delete axiosInstance.defaults.headers.common['Authorization'];
  }
};

// Function to load token from cookies
export const loadAuthToken = () => {
  const token = Cookies.get('authToken');
  if (token) {
    axiosInstance.defaults.headers.common['Authorization'] = `Bearer ${token}`;
  }
};

export default axiosInstance;
