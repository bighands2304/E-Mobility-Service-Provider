import React from "react";
import camelize from "camelize";
import customFetch from "../../../utils/axios";

export const vehicleTrasform = (results) => {
  const mappedResults = results.map((item) => {
    return {
      VehicleModel: item.vehicle.model,
      VinCode: item.vehicle.vincode,
      SocketType: item.vehicle.socketType,
      isFavourite: item.favourite,
      Range: item.vehicle.KmRange,
      Battery: item.vehicle.batteryPercentage,
    };
  });

  return camelize(mappedResults);
};

export const addVehicleRequest = async (vinCodeV, userIdV, jwt) => {
  const obj = {
    userId: userIdV,
    vin: vinCodeV,
    favourite: true,
  };

  const url = "/user/addVehicle";
  try {
    const customInstance = customFetch(jwt);
    const resp = await customInstance.post(url, obj);

    const results = resp.data;
    return results;
  } catch (error) {
    console.log("error" + error);
  }
};

export const getVehicleRequest = async (idUser, jwt) => {
  const url = `/user/getUserVehicles/${idUser}`;

  try {
    const customInstance = customFetch(jwt);
    const resp = await customInstance.get(url);
    const results = resp.data.map((d) => d.vehicle);

    return resp.data;
  } catch (error) {
    console.log("error" + error);
  }
};

export const deleteVehicleRequest = async (vinCode, jwt) => {
  const url = `user/deleteVehicle/${vinCode}`;
  try {
    const customInstance = customFetch(jwt);
    const resp = await customInstance.delete(url);
  } catch (error) {
    console.log(JSON.stringify(error));
  }
};
