import React from "react";
import { mocks, mockImages } from "./mock";
import camelize from "camelize";
import customFetch from "../../utils/axios";

export const restaurantsRequest = async (location) => {
  console.log(
    "this is the loc that arrive to Request in serice" +
      JSON.stringify(location)
  );
  const latS = location.result.viewport.southwest.lat;
  const lonS = location.result.viewport.southwest.lng;
  const distS =
    location.result.viewport.northeast.lat -
    location.result.viewport.southwest.lat;

  console.log("JJJ");
  console.log("latS, lonS, distS", latS, lonS, distS);
  console.log("JJJ");

  const url = `/user/getCPsInRange/${latS}/${lonS}/${distS}`;
  console.log("uuuuu");
  console.log(url);
  try {
    const resp = await customFetch.get(url);
    console.log("resappp" + resp);
    console.log("resappp" + resp);
    console.log("resappp" + resp);
    console.log("resappp" + resp);
    console.log(JSON.stringify(resp.data));
    const cp = resp.data;
    const photo_array = [
      "http://cadonia.com/wp-content/uploads/2020/05/ev-charging-stations-drafting-services1-1.jpg",
      "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRjnl6goM7Pc4zGN3kxCMCzaGfHqA96YDzwxg&usqp=CAU",
    ];
    const updated_cp = cp.map((item) => {
      return { ...item, photo: photo_array };
    });
    console.log("cp clean" + JSON.stringify(updated_cp));
    console.log("cp clean" + JSON.stringify(updated_cp));
    console.log("cp clean" + JSON.stringify(updated_cp));
    console.log("cp clean" + JSON.stringify(updated_cp));

    return updated_cp;
  } catch (error) {
    console.log(error);
    console.log(error.message);
    console.log(JSON.stringify(error));
    console.log("bad ass");
  }
};

export const restaurantsTransform = (result) => {
  return result;
};
