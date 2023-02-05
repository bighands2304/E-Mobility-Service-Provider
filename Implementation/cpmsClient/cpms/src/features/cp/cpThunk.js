import customFetch from "../../utils/axios";

export const addCpThunk = async (obj, thunkAPI) => {
  try {
    await customFetch.post("/chargingPoints", obj);
  } catch (error) {
    console.log(JSON.stringify(error));
    return thunkAPI.rejectWithValue(error.message);
  }
};
