import customFetch from "../../utils/axios";

export const getReservationRequest = async (usId, jwt) => {
  const url = `/user/getReservationsOfUser/${usId}`;
  console.log("asking with jwt" + jwt);
  const customInstance = customFetch(jwt);
  const resp = await customInstance.get(url);

  const data = resp.data;

  const updatedData = data.map(async (item) => {
    const cpId = item.cpId;
    const tariffId = item.tariffId;

    const urlcp = `/user/getCP/${cpId}`;
    const cp = await customInstance.get(urlcp);

    const urlt = `/user/getTariff/${tariffId}`;
    const tariff = await customInstance.get(urlt);

    return { ...item, cp: cp.data, tariff: tariff.data };
  });

  return Promise.all(updatedData);
};
export const doReservationRequest = async (sId, cId, id, token) => {
  const url = "/user/makeReservation";
  const customInstance = customFetch(token);
  const obj = {
    userId: id,
    cpId: cId,
    socketId: sId,
  };
  const resp = customInstance.post(url, obj);
  return resp;
};

// ok?
export const deleteReservationRequest = async (reservationId, token) => {
  const url = `/user/deleteReservation/${reservationId}`;
  const customInstance = customFetch(token);
  const resp = await customInstance.delete(url);
  return resp;
};

export const startSessionRequest = async (rId, token) => {
  const url = "/user/startChargingSession";
  const customInstance = customFetch(token);
  const obj = {
    reservationId: rId,
  };

  const resp = await customInstance.post(url, obj);
  return resp;
};

//ok?
export const endSessionRequest = async (resId, token) => {
  console.log("here");
  console.log("here");
  console.log("here");
  console.log("here");
  console.log("here");
  console.log("here");
  console.log("here");
  console.log("here");
  const url = "/user/endSession";
  const obj = {
    reservationId: resId,
  };
  try {
    const customInstance = customFetch(token);
    const resp = await customInstance.post(url, obj);
    console.log(JSON.stringify(resp));
    return resp;
  } catch (err) {
    console.log("BBBB");
    console.log("BBBB");
    console.log("BBBB");
    console.log("BBBB");
    console.log("BBBB");
    console.log("BBBB");
    console.log("BBBB");
    console.log("BBBB");
    console.log(JSON.stringify(err));
  }
};
