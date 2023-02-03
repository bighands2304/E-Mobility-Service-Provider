import { StatusBar as ExpoStatusBar } from "expo-status-bar";
import React from "react";
import { initializeApp } from "firebase/app";
import { ThemeProvider } from "styled-components/native";
import { AuthenticationContextProvider } from "./src/services/authentication/authentication.context";
import { Navigation } from "./src/infrastructure/navigation";
import {
  useFonts as useOswald,
  Oswald_400Regular,
} from "@expo-google-fonts/oswald";
import { useFonts as useLato, Lato_400Regular } from "@expo-google-fonts/lato";
import { theme } from "./src/infrastructure/theme";

// Import the functions you need from the SDKs you need

// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
const firebaseConfig = {
  apiKey: "AIzaSyD_d--ufPc20IjhKZg6efYsf3LK9ldFd98",
  authDomain: "emall-e4ddd.firebaseapp.com",
  projectId: "emall-e4ddd",
  storageBucket: "emall-e4ddd.appspot.com",
  messagingSenderId: "652550845613",
  appId: "1:652550845613:web:a6fd0d9a84c77c82544224",
};
// Initialize Firebase
initializeApp(firebaseConfig);

export default function App() {
  const [oswaldLoaded] = useOswald({
    Oswald_400Regular,
  });

  const [latoLoaded] = useLato({
    Lato_400Regular,
  });

  if (!oswaldLoaded || !latoLoaded) {
    return null;
  }
  return (
    <>
      <ThemeProvider theme={theme}>
        <AuthenticationContextProvider>
          <Navigation />
        </AuthenticationContextProvider>
      </ThemeProvider>

      <ExpoStatusBar style="auto" />
    </>
  );
}
