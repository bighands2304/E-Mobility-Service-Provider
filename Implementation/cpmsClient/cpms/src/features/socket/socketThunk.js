import customFetch from "../../utils/axios";

export const changeAvailability = async (message, url, thunkAPI) => {
  try {
    const resp = await customFetch.patch(url, message);
    return resp;
  } catch (error) {
    return error.response;
  }
};
