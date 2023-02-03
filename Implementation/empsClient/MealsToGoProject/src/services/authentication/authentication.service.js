import axios from "axios";
import { signInWithEmailAndPassword } from "firebase/auth";
import customFetch from "../../utils/axios";
import { addTokenToLocalStorage } from "../../utils/localStorage";

export const loginRequest = async (email, password) => {
  const obj = {
    username: email,
    password: password,
  };
  const url = "/login";
  try {
    const resp = await customFetch.post(url, obj);
    console.log("resp" + JSON.stringify(resp));
    const jwt = resp.data.jwt;
    const user = resp.data.user;
    addTokenToLocalStorage(jwt);
    addTokenToLocalStorage(user);
    return user;
  } catch (error) {
    return error.message;
  }
};

export const registerRequest = async (
  newEmail,
  mewUsername,
  newPassword,
  newName,
  newSurname
) => {
  const obj = {
    email: newEmail,
    username: mewUsername,
    password: newPassword,
    name: newName,
    surname: newSurname,
  };
  const url = "/register";
  try {
    const resp = await customFetch.post(url, obj);
    return resp.data;
  } catch (error) {
    console.log("error" + error);
  }
};
