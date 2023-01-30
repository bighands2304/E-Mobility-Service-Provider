import { BrowserRouter, Routes, Route } from "react-router-dom";
import { Error, Landing, Register, ProtectedRoute } from "./pages";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import {
  Profile,
  Map,
  Offers,
  AllCps,
  AddCp,
  Stats,
  SharedLayout,
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
          <Route index element={<Stats />} />
          <Route path="map" element={<Map />} />
          <Route path="offers" element={<Offers />} />
          <Route path="profile" element={<Profile />} />
          <Route path="all-cps" element={<AllCps />} />
          <Route path="add-cp" element={<AddCp />} />
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
