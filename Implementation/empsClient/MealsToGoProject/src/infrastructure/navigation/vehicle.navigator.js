import React from "react";
import { SafeArea } from "../../components/utility/safe-area.component";

import {
  createStackNavigator,
  TransitionPresets,
} from "@react-navigation/stack";

import { VehiclesScreen } from "../../features/vehicles/vehicles.screen";
import { VehicleDetailScreen } from "../../features/vehicles/vehcles-detail.screen";
import { AddNewVehicle } from "../../features/vehicles/addnewvehicle.screen";

const VehiclesStack = createStackNavigator();

export const VehicleNavigator = () => {
  return (
    <VehiclesStack.Navigator
      screenOptions={{
        headerShown: false,
        ...TransitionPresets.ModalPresentationIOS,
      }}
    >
      <VehiclesStack.Screen name="VehiclesMain" component={VehiclesScreen} />
      <VehiclesStack.Screen
        name="VehicleDetail"
        component={VehicleDetailScreen}
      />

      <VehiclesStack.Screen name="AddVehicle" component={AddNewVehicle} />
    </VehiclesStack.Navigator>
  );
};
