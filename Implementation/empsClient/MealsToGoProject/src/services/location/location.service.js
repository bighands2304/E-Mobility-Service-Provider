import camelize from "camelize";

import { locations } from "./location.mock";
import opencage from "opencage-api-client";

export const locationRequest = (searchTerm) => {
  return new Promise((resolve, reject) => {
    // const locationMock = locations[searchTerm];
    //onsole.log("location Mock ===> " + JSON.stringify(locationMock));
    const result = searchAddress(searchTerm);
    if (!result) {
      reject("not found");
    }
    resolve(result);
  });
};

export const locationTransform = (result) => {
  //console.log("sono nel location Trasform");
  // console.log(result);
  return { result };
};

const searchAddress = async (address) => {
  const res = await opencage.geocode({
    q: address,
    key: "a9f061754c7a406b8536b97bee7ffb8f",
  });
  const coordinates = res.results[0].geometry;
  // console.log("one position==>" + JSON.stringify(res.results[0].bounds));
  //console.log("latitude:" + coordinates.lat);
  //console.log("longitude:" + coordinates.lng);
  const lat = coordinates.lat;
  const lng = coordinates.lng;
  const viewport = res.results[0].bounds;
  //  console.log("viewport==>" + JSON.stringify(viewport));
  // console.log("southwest ==> " + viewport.southwest);
  //console.log("northeast ==> " + viewport.northeast);
  return { lat, lng, viewport };
};
