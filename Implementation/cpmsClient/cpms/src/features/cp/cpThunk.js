import customFetch from "../../utils/axios";

export const addCpThunk = async (obj, thunkAPI) => {
  try {
    console.log("object sending" + JSON.stringify(obj));
    await customFetch.post("/chargingPoints", obj);
  } catch (error) {
    console.log("erorrororoorororr");
    console.log(JSON.stringify(error));
    return thunkAPI.rejectWithValue(error.message);
  }
};
