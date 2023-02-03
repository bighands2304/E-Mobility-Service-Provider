import React, { useState, useContext, createContext, useEffect } from "react";
import { AuthenticationContext } from "../../authentication/authentication.context";

import {
  vehicleRequest,
  vehicleTrasform,
  addVehicleRequest,
} from "./vehicle.serice";

export const VehicleContext = createContext();

export const VehicleContextProvider = ({ children }) => {
  const [vehicles, setVehicles] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  // is just for try
  const { id } = useContext(AuthenticationContext);
  const {add}

  const RetrieveVehciles = (uid) => {
    setIsLoading(true);
    setVehicles([]);

    vehicleRequest(uid)
      .then(vehicleTrasform)
      .then((results) => {
        setIsLoading(false);
        setVehicles(results);
      })
      .catch((err) => {
        console.log("error there" + err);
        setIsLoading(false);
        setError(err);
      });
  };

  const AddVehicle = (vinCode) => {
    setIsLoading(true);
    addVehicleRequest(vinCode, id)
      .then((resul) => {
        setIsLoading(false);
        RetrieveVehciles(id);
      })
      .catch((err) => {
        console.log("error there" + err);
        setIsLoading(false);
        setError(err);
      });
  };

  useEffect(() => {
    if (id) {
      RetrieveVehciles(id);
      // console.log("finish Retrive vehicle");
    }
  }, [id]);

  // console.log("auid " + id);
  //console.log("vehicles " + vehicles);

  return (
    <VehicleContext.Provider
      value={{
        vehicles,
        isLoading,
        error,
        AddVehicle,
      }}
    >
      {children}
    </VehicleContext.Provider>
  );
};
