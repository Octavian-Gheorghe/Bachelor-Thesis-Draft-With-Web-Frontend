const API_BASE_URL = process.env.NODE_ENV === 'development'
  ? 'http://localhost:8080'
  : ''; // or your deployed backend URL

export default API_BASE_URL;