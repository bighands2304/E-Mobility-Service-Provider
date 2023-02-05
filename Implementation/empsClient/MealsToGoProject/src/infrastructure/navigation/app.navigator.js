import React from "react";
import { createBottomTabNavigator } from "@react-navigation/bottom-tabs";
import { Ionicons } from "@expo/vector-icons";
import { CPsNavigator } from "./cps.navigator";
import { MapScreen } from "../../features/map/screen/map.screen";
import { SettingsNavigator } from "./settings.navigator";
import { VehicleNavigator } from "./vehicle.navigator";
import { SessionsScreen } from "../../features/sessions/screen/session.screen";

import { FavouritesContextProvider } from "../../services/favourites/favourites.context";
import { CPsContextProvider } from "../../services/cps/cps.context";
import { LocationContextProvider } from "../../services/location/location.context";
import { VehicleContextProvider } from "../../services/vehicles/mock/vehcile.context";
import { ReservationContextProvider } from "../../services/reservation/reservation.context";

const Tab = createBottomTabNavigator();

const TAB_ICON = {
  Stations: "battery-charging-outline",
  Map: "md-map",
  Settings: "md-settings",
  Vehicles: "car-outline",
  Sessions: "wallet-outline",
};

const createScreenOptions = ({ route }) => {
  const iconName = TAB_ICON[route.name];
  return {
    tabBarIcon: ({ size, color }) => (
      <Ionicons name={iconName} size={size} color={color} />
    ),
    tabBarOptions: {
      activeTintColor: "tomato",
      inactiveTintColor: "gray",
    },
  };
};

export const AppNavigator = () => (
  <VehicleContextProvider>
    <FavouritesContextProvider>
      <LocationContextProvider>
        <CPsContextProvider>
          <ReservationContextProvider>
            <Tab.Navigator screenOptions={createScreenOptions}>
              <Tab.Screen name="Vehicles" component={VehicleNavigator} />
              <Tab.Screen name="Stations" component={CPsNavigator} />
              <Tab.Screen name="Map" component={MapScreen} />
              <Tab.Screen name="Sessions" component={SessionsScreen} />
              <Tab.Screen name="Settings" component={SettingsNavigator} />
            </Tab.Navigator>
          </ReservationContextProvider>
        </CPsContextProvider>
      </LocationContextProvider>
    </FavouritesContextProvider>
  </VehicleContextProvider>
);
