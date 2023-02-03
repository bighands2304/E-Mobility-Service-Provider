import React from "react";
import { mocks, mockImages } from "./mock";
import camelize from "camelize";

export const restaurantsRequest = (location) => {
  return new Promise((resolve, reject) => {
    const jsonData = require("./mock/charginpoint.json");
    const result = jsonData.results;

    if (!result) {
      console.log("error");
      reject("not found");
    }
    resolve(result);
  });
};

export const restaurantsTransform = (result) => {
  return result;
};
