import React, { useState, createContext, useEffect } from "react";
import axios from "axios";

import { loginRequest, registerRequest } from "./authentication.service";
import { createOnButtonAlert } from "../../components/utility/Alert";

export const AuthenticationContext = createContext();

export const AuthenticationContextProvider = ({ children }) => {
  const [isLoading, setIsLoading] = useState(false);
  const [user, setUser] = useState(null);
  // this is just for try
  const [id, setId] = useState(-1);
  const [error, setError] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [token, setToken] = useState();

  useEffect(() => {
    if (id < 0) {
      setIsAuthenticated(false);
    } else {
      setIsAuthenticated(true);
    }
  }, [id]);

  const onLogin = (email, password) => {
    setIsLoading(true);
    loginRequest(email, password)
      .then((u) => {
        setUser(u.user);
        setToken(u.jwt);
        setId(u.user.id);

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
    setUser(null);
    setError(null);
    setId(-1);
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
