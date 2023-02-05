import React, { createContext, useState, useEffect, useContext } from "react";
import AsyncStorage from "@react-native-async-storage/async-storage";
import { AuthenticationContext } from "../authentication/authentication.context";
import { VehicleContext } from "../vehicles/mock/vehcile.context";

export const FavouritesContext = createContext();

export const FavouritesContextProvider = ({ children }) => {
  const { user } = useContext(AuthenticationContext);
  const { vehicles } = useContext(VehicleContext);
  const [favourite, setFavourite] = useState([]);
  const [socketF, setsocketF] = useState([]);

  const SetNewF = () => {
    vehicles.forEach((vehicle) => {
      if (vehicle.isFavourite) {
        console.log("FAVAFAAAVA");
        console.log("FAVAFAAAVA");
        console.log("FAVAFAAAVA");
        console.log("FAVAFAAAVA");
        console.log("FAVAFAAAVA");
        console.log("FAVAFAAAVA");
        console.log("FAVAFAAAVA");
        console.log(vehicle.SocketType);
        setsocketF(vehicle.SocketType);
        setFavourite(vehicle);
      }
    });
  };

  useEffect(() => {
    if (vehicles) {
      SetNewF();
    }
  }, [vehicles]);

  return (
    <FavouritesContext.Provider value={{ favourite, socketF }}>
      {children}
    </FavouritesContext.Provider>
  );
};
