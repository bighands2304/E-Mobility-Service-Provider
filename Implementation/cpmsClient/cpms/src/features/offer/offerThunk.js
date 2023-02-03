import customFetch from "../../utils/axios";

export const getAllOffersThunk = async (url, thunkAPI) => {
  try {
    const resp = await customFetch.get(url, thunkAPI);
    return resp;
  } catch (error) {
    return error.message;
  }
};

export const setOfferThunk = () => {
  return {};
};
