import axios from "axios";

const customFetch = (token) => {
  const instance = axios.create({
    baseURL: "https://emspserver.up.railway.app/",
  });

  instance.interceptors.request.use((config) => {
    if (token) {
      config.headers["Authorization"] = `Bearer ${token}`;
    }
    return config;
  });

  return instance;
};

export default customFetch;
