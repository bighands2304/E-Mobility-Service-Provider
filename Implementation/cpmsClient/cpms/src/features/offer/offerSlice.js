import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { toast } from "react-toastify";
import { getAllOffersThunk, setOfferThunk } from "./offerThunk";

const initialState = {
  offerlist: [],
  cpId: 0,
  isLoading: true,
};

export const selectOffer = createAsyncThunk(
  "setOffer",
  async (offer, info, thunkAPI) => {
    const url = `/dso/offers/${offer.offerId}`;
    return setOfferThunk(url, info, thunkAPI);
  }
);

export const getAllOffers = createAsyncThunk("getAll", async (cp, thunkAPI) => {
  const url = `/chargingPoints/${cp.id}/dso/offers`;
  return getAllOffersThunk(url, thunkAPI);
});

const offerSlice = createSlice({
  name: "offers",
  initialState,
  reducers: {
    showLoading: (state) => {
      state.isLoading = true;
    },
    hideLoading: (state) => {
      state.isLoading = false;
    },
    setcpId: (state, action) => {
      state.cpId = action.payload;
    },
  },

  extrareducers: (builder) => {
    builder
      .addCase(selectOffer.pending, (state) => {
        state.isLoading = true;
      })
      .addCase(selectOffer.fulfilled, (state, { payload }) => {
        const { offer } = payload;
        state.isLoading = false;
        toast.success("sucess activated new offer " + offer.id);
      })
      .addCase(selectOffer.pending, (state) => {
        state.isLoading = false;
        toast.error("An Error Occured");
      })
      .addCase(getAllOffers.pending, (state) => {
        state.isLoading = true;
      })
      .addCase(getAllOffers.fulfilled, (state, { payload }) => {
        state.isLoading = false;
        state.offers = payload;
      })
      .addCase(getAllOffers.pending, (state) => {
        state.isLoading = false;
      });
  },
});

export const { showLoading, hideLoading, setcpId } = offerSlice.actions;

export default offerSlice.reducer;
