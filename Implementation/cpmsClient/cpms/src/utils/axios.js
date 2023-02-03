import axios from "axios";
import { clearStore } from "../features/user/userSlice";
import {
  getUserFromLocalStorage,
  getTokenFromLocalStorage,
} from "./localStorage";

const customFetch = axios.create({
  baseURL: "https://cpmsserver.up.railway.app/api/CPO",
});

customFetch.interceptors.request.use((config) => {
  const user = getUserFromLocalStorage();
  const token = getTokenFromLocalStorage();
  if (user) {
    config.headers["Authorization"] = `Bearer ${token}`;
  }
  return config;
});

export default customFetch;
