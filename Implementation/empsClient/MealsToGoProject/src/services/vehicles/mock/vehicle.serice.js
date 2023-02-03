import React from "react";
import camelize from "camelize";

export const vehicleRequest = (userId) => {
  return new Promise((resolve, reject) => {
    const jsonData = require("./data.json");
    const results = jsonData.results;
    if (!results) {
      // console.log("rejected !");
      reject("not found");
    }
    const userId1Results = results.filter(
      (result) => result.userId === userId.toString()
    );
    //  console.log(userId1Results);
    resolve(jsonData);
  });
};

export const vehicleTrasform = ({ results = [] }) => {
  const mappedResults = results.map((item) => {
    return {
      VehicleModel: item.Model,
      VinCode: item.vin,
      SocketType: item.socketType,
      isFavourite: item.favourite,
      Range: item.range,
      Battery: item.batteryStatus,
    };
  });
  return camelize(mappedResults);
};
