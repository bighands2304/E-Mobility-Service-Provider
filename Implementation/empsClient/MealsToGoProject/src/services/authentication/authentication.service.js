import axios from "axios";
import { signInWithEmailAndPassword } from "firebase/auth";
import customFetch from "../../utils/axios";

export const loginRequest = (auth, email, password) => {
  signInWithEmailAndPassword(auth, email, password);
};

import { removeUserFromLocalStorage } from "../../utils/localStorage";
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
    console.log("preee");
    const resp = await customFetch.post(url, obj);
    console.log("post");
    console.log("resp =>=>=>=>=" + JSON.stringify(resp));
    return resp.data;
  } catch (error) {
    console.log("error" + error);
  }
};
