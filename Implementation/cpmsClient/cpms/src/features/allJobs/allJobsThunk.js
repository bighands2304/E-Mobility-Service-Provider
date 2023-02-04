import customFetch from "../../utils/axios";

export const getAllCPsThunk = async (_, thunkAPI) => {
  let url = `/chargingPoints`;

  try {
    // console.log("asking all cps");
    const resp = await customFetch.get(url);
    // console.log("respoinse ==> " + JSON.stringify(resp.data));
    return resp.data;
  } catch (error) {
    return error.message;
  }
};
/*
export const showStatsThunk = async (_, thunkAPI) => {
  try {
    const resp = await customFetch.get('/jobs/stats');

    return resp.data;
  } catch (error) {
    return checkForUnauthorizedResponse(error, thunkAPI);
  }
};
*/
