import React from "react";
import { createBottomTabNavigator } from "@react-navigation/bottom-tabs";
import { Ionicons } from "@expo/vector-icons";
import { RestaurantsNavigator } from "./restaurants.navigator";
import { MapScreen } from "../../features/map/screen/map.screen";
import { SettingsNavigator } from "./settings.navigator";
import { VehicleNavigator } from "./vehicle.navigator";
import { SessionsScreen } from "../../features/sessions/screen/session.screen";

import { FavouritesContextProvider } from "../../services/favourites/favourites.context";
import { RestaurantsContextProvider } from "../../services/restaurants/restaurants.context";
import { LocationContextProvider } from "../../services/location/location.context";
import { VehicleContextProvider } from "../../services/vehicles/mock/vehcile.context";

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
        <RestaurantsContextProvider>
          <Tab.Navigator screenOptions={createScreenOptions}>
            <Tab.Screen name="Vehicles" component={VehicleNavigator} />
            <Tab.Screen name="Stations" component={RestaurantsNavigator} />
            <Tab.Screen name="Map" component={MapScreen} />
            <Tab.Screen name="Sessions" component={SessionsScreen} />
            <Tab.Screen name="Settings" component={SettingsNavigator} />
          </Tab.Navigator>
        </RestaurantsContextProvider>
      </LocationContextProvider>
    </FavouritesContextProvider>
  </VehicleContextProvider>
);
