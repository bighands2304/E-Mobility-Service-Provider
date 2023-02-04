import React, { useState, useContext, createContext, useEffect } from "react";
import { createOnButtonAlert } from "../../components/utility/Alert";
import {
  restaurantsRequest,
  restaurantsTransform,
  doReservationRequest,
} from "./restaurants.service";

import { LocationContext } from "../location/location.context";
import { AuthenticationContext } from "../authentication/authentication.context";

export const RestaurantsContext = createContext();

export const RestaurantsContextProvider = ({ children }) => {
  const [restaurants, setRestaurants] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const { location } = useContext(LocationContext);
  const { token, id } = useContext(AuthenticationContext);

  const retrieveRestaurants = (loc) => {
    setIsLoading(true);
    setRestaurants([]);
    setTimeout(() => {
      restaurantsRequest(loc, token)
        .then(restaurantsTransform)
        .then((results) => {
          setIsLoading(false);
          setRestaurants(results);
        })
        .catch((err) => {
          setRestaurants([]);
          setIsLoading(false);
          setError(err);
        });
    });
  };

  const doReservation = (socketId, cpId) => {
    setIsLoading(true);
    doReservationRequest(socketId, cpId, id, token)
      .then((results) => {
        createOnButtonAlert("Suchess", "Reservation created");
        setIsLoading(false);
      })
      .catch((err) => {
        createOnButtonAlert("An Error Occurred", "Retry Later");
        console.log("l");
        console.log("l");
        console.log("l");
        console.log("l");
        console.log("l");
        console.log(err);
        console.log(JSON.stringify(err));

        setIsLoading(false);
        setError(err);
      });
  };

  useEffect(() => {
    if (location) {
      const locationString = `${location.lat},${location.lng}`;
      retrieveRestaurants(location);
    }
  }, [location]);

  return (
    <RestaurantsContext.Provider
      value={{
        restaurants,
        isLoading,
        error,
        doReservation,
      }}
    >
      {children}
    </RestaurantsContext.Provider>
  );
};
