import React, { useState, useContext, createContext, useEffect } from "react";
import { createOnButtonAlert } from "../../components/utility/Alert";
import { cpsRequest, restaurantsTransform } from "./cps.service";

import { LocationContext } from "../location/location.context";
import { AuthenticationContext } from "../authentication/authentication.context";

export const CPsContext = createContext();

export const CPsContextProvider = ({ children }) => {
  const [cps, setCps] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const { location } = useContext(LocationContext);
  const { token, id } = useContext(AuthenticationContext);

  const retrieveCPs = (loc) => {
    setIsLoading(true);
    setCps([]);
    setTimeout(() => {
      cpsRequest(loc, token)
        .then(restaurantsTransform)
        .then((results) => {
          setIsLoading(false);
          setCps(results);
        })
        .catch((err) => {
          setCps([]);
          setIsLoading(false);
          setError(err);
        });
    });
  };

  useEffect(() => {
    if (location) {
      const locationString = `${location.lat},${location.lng}`;
      retrieveCPs(location);
    }
  }, [location]);

  return (
    <CPsContext.Provider
      value={{
        cps,
        isLoading,
        error,
      }}
    >
      {children}
    </CPsContext.Provider>
  );
};
