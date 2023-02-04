import React, { useState, createContext, useRef } from "react";
import axios from "axios";

import {
  loginRequest,
  registerRequest,
  logoutRequest,
} from "./authentication.service";
import { createOnButtonAlert } from "../../components/utility/Alert";

export const AuthenticationContext = createContext();

export const AuthenticationContextProvider = ({ children }) => {
  const [isLoading, setIsLoading] = useState(false);
  const [user, setUser] = useState(null);
  // this is just for try
  const [id, setId] = useState();
  const [error, setError] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [token, setToken] = useState();

  const onLogin = (email, password) => {
    setIsLoading(true);
    loginRequest(email, password)
      .then((u) => {
        console.log("usssusuusuususuus" + JSON.stringify(u));
        setUser(u.user);
        setToken(u.jwt);
        setIsAuthenticated(true);
        setId(u.user.id);
        console.log("now user id " + id);
        setIsLoading(false);
      })
      .catch((e) => {
        console.log("bbbbbbb");
        setIsLoading(false);
        setError(e.toString());
      });
  };

  const onRegister = (
    email,
    password,
    repeatedPassword,
    username,
    name,
    surname
  ) => {
    setIsLoading(true);
    if (password !== repeatedPassword) {
      console.log("password" + password);
      console.log("rep" + repeatedPassword);
      setError("Error: Passwords do not match");
      return;
    }
    registerRequest(email, username, password, name, surname)
      .then((u) => {
        setIsLoading(false);
        createOnButtonAlert("Success", "Go to login to continue");
      })
      .catch((e) => {
        setIsLoading(false);
        setError(e.toString());
        createOnButtonAlert("Error ", "Please retry");
        return false;
      });
  };
  const onLogout = () => {
    setIsAuthenticated(false);
    setUser(null);
    setError(null);
    setId(null);
  };

  return (
    <AuthenticationContext.Provider
      value={{
        user,
        id,
        isLoading,
        error,
        onLogin,
        onRegister,
        onLogout,
        isAuthenticated,
      }}
    >
      {children}
    </AuthenticationContext.Provider>
  );
};
