import React, { useState, useContext, createContext, useEffect } from "react";

import {
  restaurantsRequest,
  restaurantsTransform,
} from "./restaurants.service";

import { LocationContext } from "../location/location.context";

export const RestaurantsContext = createContext();

export const RestaurantsContextProvider = ({ children }) => {
  const [restaurants, setRestaurants] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const { location } = useContext(LocationContext);

  const retrieveRestaurants = (loc) => {
    console.log("this is the loc that arrive to retrieveRestaurants " + loc);
    console.log("this is the loc that arrive to retrieveRestaurants " + loc);
    console.log("this is the loc that arrive to retrieveRestaurants " + loc);
    console.log("this is the loc that arrive to retrieveRestaurants " + loc);
    console.log("this is the loc that arrive to retrieveRestaurants " + loc);
    setIsLoading(true);
    setRestaurants([]);
    setTimeout(() => {
      restaurantsRequest(loc)
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

  useEffect(() => {
    if (location) {
      const locationString = `${location.lat},${location.lng}`;
      retrieveRestaurants(location);
      console.log("location that i send" + JSON.stringify(location));
      console.log("location that i send" + JSON.stringify(location));
      console.log("location that i send" + JSON.stringify(location));
      console.log("I do it ");
      console.log("I do it ");
      console.log("I do it ");
      console.log("I do it ");

      console.log("location that i sendRRRRR" + JSON.stringify(locationString));
      console.log("location that i sendRRR" + JSON.stringify(locationString));
      console.log("location that i sendRRR" + JSON.stringify(locationString));
    }
  }, [location]);

  return (
    <RestaurantsContext.Provider
      value={{
        restaurants,
        isLoading,
        error,
      }}
    >
      {children}
    </RestaurantsContext.Provider>
  );
};
