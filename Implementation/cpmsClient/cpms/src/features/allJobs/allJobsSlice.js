import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { toast } from "react-toastify";
import { getAllCPsThunk } from "./allJobsThunk";

const initialState = {
  isLoading: true,
  cps: [],
  totalCPs: 0,
  numOfPages: 1,
  page: 1,
  stats: {},
};

export const getAllJobs = createAsyncThunk("/charingPoints", getAllCPsThunk);

//export const showStats = createAsyncThunk('allJobs/showStats', showStatsThunk);

const allJobsSlice = createSlice({
  name: "allJobs",
  initialState,
  reducers: {
    showLoading: (state) => {
      state.isLoading = true;
    },
    hideLoading: (state) => {
      state.isLoading = false;
    },
    handleChange: (state, { payload: { name, value } }) => {
      state.page = 1;
      state[name] = value;
    },

    changePage: (state, { payload }) => {
      state.page = payload;
    },
    clearAllJobsState: (state) => initialState,
  },
  extraReducers: (builder) => {
    builder
      .addCase(getAllJobs.pending, (state) => {
        state.isLoading = true;
      })
      .addCase(getAllJobs.fulfilled, (state, { payload }) => {
        state.isLoading = false;
        state.cps = payload.content;
        state.numOfPages = payload.totalPages;
        state.totalCPs = payload.totalElements;
      })
      .addCase(getAllJobs.rejected, (state, { payload }) => {
        console.log("Error");
        state.isLoading = false;
        toast.error(payload);
      });
  },
});

export const {
  showLoading,
  hideLoading,
  handleChange,
  clearFilters,
  changePage,
  clearAllJobsState,
} = allJobsSlice.actions;

export default allJobsSlice.reducer;

/*
  .addCase(showStats.pending, (state) => {
        state.isLoading = true;
      })
      .addCase(showStats.fulfilled, (state, { payload }) => {
        state.isLoading = false;
        state.stats = payload.defaultStats;
        state.monthlyApplications = payload.monthlyApplications;
      })
      .addCase(showStats.rejected, (state, { payload }) => {
        state.isLoading = false;
        toast.error(payload);
      });

*/
