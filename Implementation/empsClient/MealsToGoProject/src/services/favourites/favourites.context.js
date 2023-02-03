import React, { createContext, useState, useEffect, useContext } from "react";
import AsyncStorage from "@react-native-async-storage/async-storage";
import { AuthenticationContext } from "../authentication/authentication.context";
import { VehicleContext } from "../vehicles/mock/vehcile.context";

export const FavouritesContext = createContext();

export const FavouritesContextProvider = ({ children }) => {
  const { user } = useContext(AuthenticationContext);
  const { vehicles } = useContext(VehicleContext);
  const [favourites, setFavourites] = useState([]);

  let count = vehicles.filter(
    (vehicles) => vehicles.isFavourite === true
  ).length;

  let favVehicle;
  if (count === 1) {
    favVehicle = vehicles.filter((vehicles) => vehicles.isFavourite === true);
    // console.log("fav ==> " + JSON.stringify(favVehicle));
  }

  const add = () => {
    console.log("dummy");
  };

  const remove = () => {
    console.log("dummy");
  };

  return (
    <FavouritesContext.Provider
      value={{ favVehicle, addToFavourites: add, removeFromFavourites: remove }}
    >
      {children}
    </FavouritesContext.Provider>
  );
};
