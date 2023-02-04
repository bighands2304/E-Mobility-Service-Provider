import React, { useState, useContext, createContext, useEffect } from "react";
import { AuthenticationContext } from "../../authentication/authentication.context";
import { createOnButtonAlert } from "../../../components/utility/Alert";
import {
  getVehicleRequest,
  vehicleTrasform,
  addVehicleRequest,
  deleteVehicleRequest,
} from "./vehicle.serice";

export const VehicleContext = createContext();

export const VehicleContextProvider = ({ children }) => {
  const [vehicles, setVehicles] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  // is just for try
  const { id } = useContext(AuthenticationContext);

  const RetrieveVehciles = (uid) => {
    setIsLoading(true);
    setVehicles([]);

    getVehicleRequest(uid)
      .then(vehicleTrasform)
      .then((results) => {
        setIsLoading(false);
        setVehicles(results);
      })
      .catch((err) => {
        setIsLoading(false);
        setError(err);
      });
  };

  const AddNewVehicle = (vinCode) => {
    setIsLoading(true);
    addVehicleRequest(vinCode, id)
      .then((resul) => {
        setIsLoading(false);
        RetrieveVehciles(id);
        createOnButtonAlert("Success", "Vehicle Added");
      })
      .catch((err) => {
        console.log("error there" + err);
        setIsLoading(false);
        setError(err);
        createOnButtonAlert("Error", "Check your Vin Code");
      });
  };
  const DeleteVehcile = (vinCode) => {
    setIsLoading(true);
    deleteVehicleRequest(vinCode)
      .then((resul) => {
        setIsLoading(false);
        RetrieveVehciles(id);
        createOnButtonAlert("Success", "Vehicle Deleted");
      })
      .catch((err) => {
        console.log("error there" + err);
        setIsLoading(false);
        setError(err);
        createOnButtonAlert("Error", "Check your Vin Code");
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
        AddNewVehicle,
        DeleteVehcile,
      }}
    >
      {children}
    </VehicleContext.Provider>
  );
};
