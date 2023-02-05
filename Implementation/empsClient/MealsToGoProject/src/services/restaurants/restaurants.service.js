import React from "react";
import customFetch from "../../utils/axios";

export const restaurantsRequest = async (location, jwt) => {
  const latS = location.result.viewport.southwest.lat;
  const lonS = location.result.viewport.southwest.lng;
  const distS =
    location.result.viewport.northeast.lat -
    location.result.viewport.southwest.lat;

  const url = `/user/getCPsInRange/${latS}/${lonS}/${distS}`;

  const customInstance = customFetch(jwt);
  const resp = await customInstance.get(url);

  const cp = resp.data;
  const photo_array = [
    "http://cadonia.com/wp-content/uploads/2020/05/ev-charging-stations-drafting-services1-1.jpg",
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRjnl6goM7Pc4zGN3kxCMCzaGfHqA96YDzwxg&usqp=CAU",
  ];
  const updated_cp = cp.map((item) => {
    return { ...item, photo: photo_array };
  });

  return updated_cp;
};

// ok ?
