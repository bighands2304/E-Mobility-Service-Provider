import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { toast } from "react-toastify";
import { setTariffThunk, deleteTariffThunk } from "./tariffThunk";

export const setTariffStandard = createAsyncThunk(
  "/{chaeginPointid}/tariffs",
  async (tariff, thunkAPI, cpId) => {
    const url = `/chargingPoints/${tariff.cpId}/tariffs`;
    delete tariff.cpId;
    return setTariffThunk(url, tariff.tariff, thunkAPI);
  }
);

export const setTariffSpecial = createAsyncThunk(
  "/{chaeginPointid}/tariffs",
  async (tariff, thunkAPI, cpId) => {
    const url = `/chargingPoints/${tariff.cpId}/tariffs`;

    delete tariff.cpId;

    return setTariffThunk(url, tariff.tariff, thunkAPI);
  }
);

export const deleteTariff = createAsyncThunk("tariffId", deleteTariffThunk);

const tariffSlice = createSlice({
  name: "tariff",

  extraReducers: (builder) => {
    builder
      .addCase(setTariffStandard.pending, (state) => {
        state.isLoading = true;
        console.log("pending");
      })
      .addCase(setTariffStandard.fulfilled, (state, { payload }) => {
        state.isLoading = false;

        toast.success("Suchess");
      })
      .addCase(setTariffStandard.rejected, (state, { payload }) => {
        state.isLoading = false;
        toast.error(payload);
      })
      .addCase(setTariffSpecial.pending, (state) => {
        state.isLoading = true;
        console.log("pending");
      })
      .addCase(setTariffSpecial.fulfilled, (state, { payload }) => {
        state.isLoading = false;

        toast.success("Suchess");
      })
      .addCase(setTariffSpecial.rejected, (state, { payload }) => {
        state.isLoading = false;
        toast.error(payload);
      })
      .addCase(deleteTariff.pending, (state) => {
        state.isLoading = true;
      })
      .addCase(deleteTariff.fulfilled, (state, { payload }) => {
        state.isLoading = false;
        toast.success("Suchess");
      })
      .addCase(deleteTariff.rejected, (state, { payload }) => {
        state.isLoading = false;
        toast.error(payload);
      });
  },
});

export const { clearValues } = tariffSlice.actions;
export default tariffSlice.reducer;
