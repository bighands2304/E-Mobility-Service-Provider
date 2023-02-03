import React, { useState, createContext, useRef } from "react";
import { signOut, onAuthStateChanged, getAuth } from "firebase/auth";
import { loginRequest, registerRequest } from "./authentication.service";
import { createOnButtonAlert } from "../../components/utility/Alert";

export const AuthenticationContext = createContext();

export const AuthenticationContextProvider = ({ children }) => {
  const [isLoading, setIsLoading] = useState(false);
  const [user, setUser] = useState(null);
  // this is just for try
  const [id, setId] = useState(1);
  const [error, setError] = useState(null);
  const auth = useRef(getAuth()).current;

  onAuthStateChanged(auth, (usr) => {
    if (usr) {
      setUser(usr);
      setIsLoading(false);
    } else {
      setIsLoading(false);
    }
  });
  const onLogin = (email, password) => {
    setIsLoading(true);
    loginRequest(auth, email, password)
      .then((u) => {
        setUser(u);
        setIsLoading(false);
      })
      .catch((e) => {
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
    signOut(auth).then(() => {
      setUser(null);
      setError(null);
    });
  };

  return (
    <AuthenticationContext.Provider
      value={{
        isAuthenticated: !!user,
        user,
        id,
        isLoading,
        error,
        onLogin,
        onRegister,
        onLogout,
      }}
    >
      {children}
    </AuthenticationContext.Provider>
  );
};
