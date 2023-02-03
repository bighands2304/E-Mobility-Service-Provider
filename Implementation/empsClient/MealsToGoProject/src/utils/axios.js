import axios from "axios";
import {
  getUserFromLocalStorage,
  getTokenFromLocalStorage,
} from "./localStorage";

const customFetch = axios.create({
  baseURL: "https://emspserver.up.railway.app/",
});

customFetch.interceptors.request.use(async (config) => {
  const [user, token] = await Promise.all([
    getUserFromLocalStorage(),
    getTokenFromLocalStorage(),
  ]);

  if (user) {
    config.headers["Authorization"] = `Bearer ${token}`;
  }
  return config;
});

export default customFetch;
