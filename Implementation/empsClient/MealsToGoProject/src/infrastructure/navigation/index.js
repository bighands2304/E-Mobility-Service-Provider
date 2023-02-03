import React, { useContext } from "react";
import { NavigationContainer } from "@react-navigation/native";

import { AppNavigator } from "../navigation/app.navigator";
import { AccountNavigator } from "../navigation/account.navigator";

import { AuthenticationContext } from "../../services/authentication/authentication.context";

export const Navigation = () => {
  const { isAuthenticated } = useContext(AuthenticationContext);

  return (
    <NavigationContainer>
      {isAuthenticated ? <AppNavigator /> : <AccountNavigator />}
    </NavigationContainer>
  );
};
