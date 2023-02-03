import customFetch from "../../utils/axios";
import { toast } from "react-toastify";

export const ToogleOptimizerThunk = async (url, thunkAPI) => {
  try {
    const resp = await customFetch.post(url);

    return resp;
  } catch (error) {
    return error.response;
  }
};

export default ToogleOptimizerThunk;
