import customFetch from "../../utils/axios";
import { clearAllJobsState } from "../allCPs/allCPsSlice";
import { clearValues } from "../cp/cpSlice";
import { logoutUser } from "./userSlice";
export const registerUserThunk = async (url, user, thunkAPI) => {
  try {
    const resp = await customFetch.post(url, user);
    return;
  } catch (error) {
    return thunkAPI.rejectWithValue(error.response.data.msg);
  }
};

export const loginUserThunk = async (url, user, thunkAPI) => {
  try {
    const resp = await customFetch.post(url, user);
    return resp.data;
  } catch (error) {
    return thunkAPI.rejectWithValue(error.response.data.msg);
  }
};

export const updateUserThunk = async (url, password, thunkAPI) => {
  try {
    const resp = await customFetch.post(url, password);

    return;
  } catch (error) {
    return thunkAPI.rejectWithValue(error.body.message);
  }
};

export const clearStoreThunk = async (message, thunkAPI) => {
  try {
    thunkAPI.dispatch(logoutUser(message));

    return Promise.resolve();
  } catch (error) {
    return Promise.reject();
  }
};
