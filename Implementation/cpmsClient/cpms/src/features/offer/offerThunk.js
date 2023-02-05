import customFetch from "../../utils/axios";

export const getAllOffersThunk = async (url, thunkAPI) => {
  try {
    console.log("d");
    console.log("object sending" + JSON.stringify(url));
    const resp = await customFetch.get(url, thunkAPI);
    console.log(JSON.stringify(resp.data));
    return resp.data;
    return thunkAPI.full(error.body.message);
  } catch (error) {
    console.log("failed");
    return thunkAPI.rejectWithValue(error.body.message);
  }
};

export const setOfferThunk = async (url, obj, thunkAPI) => {
  try {
    console.log("d");
    console.log("object sending" + JSON.stringify(url));
    const resp = await customFetch.pathc(url, obj);
    console.log(JSON.stringify(resp.data));
    return resp.data;
  } catch (error) {
    console.log("failed");
    return thunkAPI.rejectWithValue(error.body.message);
  }
};
