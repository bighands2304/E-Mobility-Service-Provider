import { configureStore } from "@reduxjs/toolkit";
import userSlice from "./features/user/userSlice";
import offerSlice from "./features/offer/offerSlice";
import cpSlice from "./features/cp/cpSlice";
import allCPsSlice from "./features/allCPs/allCPsSlice";
export const store = configureStore({
  reducer: {
    user: userSlice,
    cp: cpSlice,
    allCPs: allCPsSlice,
    offers: offerSlice,
  },
});
