import { useState } from "react";
import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { toast } from "react-toastify";
import { getUserFromLocalStorage } from "../../utils/localStorage";
import { addCpThunk } from "./jobThunk";
const initialState = {
  isLoading: false,
};

export const addCpT = createAsyncThunk("cpo/addCp", addCpThunk);

const jobSlice = createSlice({
  name: "job",
  initialState,

  extraReducers: (builder) => {
    builder
      .addCase(addCpT.pending, (state) => {
        state.isLoading = true;
      })
      .addCase(addCpT.fulfilled, (state) => {
        state.isLoading = false;
        toast.success("Job Created");
      })
      .addCase(addCpT.rejected, (state, { payload }) => {
        state.isLoading = false;
        console.log("error there");
        toast.error(payload);
      });
  },
});

export const {} = jobSlice.actions;

export default jobSlice.reducer;
