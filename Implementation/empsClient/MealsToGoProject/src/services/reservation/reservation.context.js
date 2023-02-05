import React, { useState, useContext, createContext, useEffect } from "react";
import { AuthenticationContext } from "../authentication/authentication.context";
import { createOnButtonAlert } from "../../components/utility/Alert";
import {
  getReservationRequest,
  doReservationRequest,
  deleteReservationRequest,
  startSessionRequest,
  endSessionRequest,
} from "./reservation.service";

export const ReservationContext = createContext();

export const ReservationContextProvider = ({ children }) => {
  const [reservations, setReservations] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  // is just for try
  const { id, token } = useContext(AuthenticationContext);

  const RetrieveReservation = (uid) => {
    console.log("reservation r for uid ==> " + uid);
    setIsLoading(true);
    setReservations([]);

    getReservationRequest(uid, token)
      .then((results) => {
        setIsLoading(false);
        setReservations(results);
        //setVehicles(results);
      })
      .catch((err) => {
        console.log("error error eror");
        console.log(err);
        setIsLoading(false);
        console.log(JSON.stringify(err));
      });
  };
  const doReservation = (socketId, cpId) => {
    setIsLoading(true);
    doReservationRequest(socketId, cpId, id, token)
      .then((results) => {
        createOnButtonAlert("Suchess", "Reservation created");
        try {
          RetrieveReservation(id);
        } catch (error) {
          console.log(JSON.stringify(error));
        }
        setIsLoading(false);
      })
      .catch((err) => {
        createOnButtonAlert("An Error Occurred ", "Retry Later");
        console.log(JSON.stringify(err));

        setIsLoading(false);
        setError(err);
      });
  };
  const doReservationDelete = async (reservationId) => {
    setIsLoading(true);
    deleteReservationRequest(reservationId, token)
      .then((results) => {
        createOnButtonAlert("Suchess", "Reservation deleted");
        try {
          RetrieveReservation(id);
        } catch (error) {
          console.log(JSON.stringify(error));
        }
        setIsLoading(false);
      })
      .catch((err) => {
        createOnButtonAlert("An Error Occurred", "Retry Later");
        console.log(JSON.stringify(err));
        setIsLoading(false);
        setError(err);
      });
  };

  const doEndSession = async (reservationId) => {
    setIsLoading(true);
    endSessionRequest(reservationId, token)
      .then((results) => {
        createOnButtonAlert("Suchess", "Session ended Correctly");
        try {
          RetrieveReservation(id);
        } catch (error) {
          console.log(JSON.stringify(error));
        }
        setIsLoading(false);
      })
      .catch((err) => {
        createOnButtonAlert("An Error Occurred =(", "Retry Later");
        console.log(JSON.stringify(err));
        setIsLoading(false);
        setError(err);
      });
  };

  const doStartChargingProcess = async (reservationId) => {
    console.log("starting");
    console.log("starting");
    console.log("starting");
    console.log("starting");
    console.log("starting");
    setIsLoading(true);
    startSessionRequest(reservationId, token)
      .then((results) => {
        createOnButtonAlert("Suchess", "Reservation Start");
        try {
          RetrieveReservation(id);
        } catch (error) {
          console.log(JSON.stringify(error));
        }
        setIsLoading(false);
      })
      .catch((err) => {
        createOnButtonAlert("An Error Occurred", "Retry Later");
        console.log(JSON.stringify(err));
        setIsLoading(false);
        setError(err);
      });
  };

  useEffect(() => {
    if (id) {
      RetrieveReservation(id);
    }
  }, [id]);

  // console.log("auid " + id);
  //console.log("vehicles " + vehicles);

  return (
    <ReservationContext.Provider
      value={{
        isLoading,
        error,
        reservations,
        doReservation,
        doReservationDelete,
        doStartChargingProcess,
        doEndSession,
      }}
    >
      {children}
    </ReservationContext.Provider>
  );
};
