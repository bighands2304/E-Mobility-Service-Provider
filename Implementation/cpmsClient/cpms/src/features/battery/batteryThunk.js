import customFetch from "../../utils/axios";
import { toast } from "react-toastify";

export const changeBatteryAvailability = async (url, thunkAPI) => {
  try {
    await customFetch.post(url);
  } catch (error) {
    return thunkAPI.rejectWithValue(error.body.message);
  }
};

export const settingBattery = async (batteryInfo, url, thunkAPI) => {
  try {
    await customFetch.patch(url, batteryInfo, thunkAPI);
  } catch (error) {
    return thunkAPI.rejectWithValue(error.body.message);
  }
};
