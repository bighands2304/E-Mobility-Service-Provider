import customFetch from "../../utils/axios";
import { toast } from "react-toastify";

export const setTariffThunk = async (url, tariff, thunkAPI) => {
  try {
    const resp = await customFetch.post(url, tariff);
    toast.success("Succhess");
    return resp.status;
  } catch (error) {
    console.log("Error");
    console.log(error);
    return thunkAPI.rejectWithValue(error);
  }
};

export const deleteTariffThunk = async (urlbase, tariff, thunkAPI) => {
  try {
    const url = urlbase;

    return;
  } catch (error) {
    return thunkAPI.rejectWithValue(error.body.message);
  }
};
