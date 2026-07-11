import axios from 'axios';

const API = axios.create({
  baseURL: '/',
  headers: {
    'Content-Type': 'application/json'
  }
});

// Automatically inject JWT access tokens into request streams
API.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('uni_hub_token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

export default API;
