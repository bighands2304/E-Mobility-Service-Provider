import { BrowserRouter, Routes, Route } from "react-router-dom";

import { Landing, Error, Register, ProtectedRoute } from "./pages";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import {
  Profile,
  AddCp,
  AllCps,
  Stats,
  SharedLayout,
  AllOffers,
  CpDetail,
  OptimizerManualSettingsBattery,
  OptimizerManualSettingsEnergyTariff,
  OptimizerManualSettingsEnergyAquisition,
} from "./pages/dashboard";
function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route
          path="/"
          element={
            <ProtectedRoute>
              <SharedLayout />
            </ProtectedRoute>
          }
        >
          <Route path="all-cps" element={<AllCps />} />
          <Route path="add-cp" element={<AddCp />} />
          <Route path="profile" element={<Profile />} />
          <Route path="cp-details" element={<CpDetail />} />
          <Route path="all-offers" element={<AllOffers />} />
          <Route
            path="opt-battery"
            element={<OptimizerManualSettingsBattery />}
          />
          <Route
            path="opt-tariff"
            element={<OptimizerManualSettingsEnergyTariff />}
          />
          <Route
            path="opt-aquisition"
            element={<OptimizerManualSettingsEnergyAquisition />}
          />
        </Route>
        <Route path="landing" element={<Landing />} />
        <Route path="register" element={<Register />} />
        <Route path="*" element={<Error />} />
      </Routes>
      <ToastContainer position="top-center" />
    </BrowserRouter>
  );
}

export default App;
