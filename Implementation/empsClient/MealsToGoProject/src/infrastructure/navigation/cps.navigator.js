import React from "react";
import { Text } from "react-native";
import {
  createStackNavigator,
  TransitionPresets,
} from "@react-navigation/stack";

import { CPScreen } from "../../features/cps/screen/cp.screen";
import { CPDetailScreen } from "../../features/cps/screen/cp-detail.screen";

const CPStack = createStackNavigator();

export const CPsNavigator = () => {
  return (
    <CPStack.Navigator
      screenOptions={{
        headerShown: false,
        ...TransitionPresets.ModalPresentationIOS,
      }}
    >
      <CPStack.Screen name="CP" component={CPScreen} />
      <CPStack.Screen name="CPDetail" component={CPDetailScreen} />
    </CPStack.Navigator>
  );
};
