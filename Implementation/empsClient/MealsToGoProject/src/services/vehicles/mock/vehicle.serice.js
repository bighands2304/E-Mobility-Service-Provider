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

export const addVehicleRequest = async (vinCodeV, userIdV) => {
  const obj = {
    userId: userIdV,
    vin: vinCodeV,
    favourite: true,
  };

  const url = "/user/addVehicle";
  try {
    const resp = await customFetch.post(url, obj);

    const results = resp.data;
    return results;
  } catch (error) {
    console.log("error" + error);
  }
};

export const getVehicleRequest = async (idUser) => {
  const url = `/user/getUserVehicles/${idUser}`;

  try {
    const resp = await customFetch.get(url);
    const results = resp.data.map((d) => d.vehicle);

    return resp.data;
  } catch (error) {
    console.log("error" + error);
  }
};

export const deleteVehicleRequest = async (vinCode) => {
  const url = `user/deleteVehicle/${vinCode}`;
  try {
    const resp = await customFetch.delete(url);
  } catch (error) {
    console.log(JSON.stringify(error));
  }
};
