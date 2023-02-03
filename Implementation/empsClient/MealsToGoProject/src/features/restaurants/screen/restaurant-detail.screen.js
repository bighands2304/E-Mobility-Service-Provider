import React, { useState } from "react";
import { ScrollView } from "react-native";
import { List } from "react-native-paper";
import { MyList3Button } from "../../../components/smartList/smartlist.component";

import { RestaurantInfoCard } from "../component/restaurant-info-card.component";

import { SafeArea } from "../../../components/utility/safe-area.component";
import { socketTariff } from "../../../components/utility/socketTariffextractor";

export const RestaurantDetailScreen = ({ navigation, route }) => {
  const [breakfastExpanded, setBreakfastExpanded] = useState(false);
  const [lunchExpanded, setLunchExpanded] = useState(false);
  const [dinnerExpanded, setDinnerExpanded] = useState(false);
  const [drinksExpanded, setDrinksExpanded] = useState(false);
  const { restaurant } = route.params;
  console.log("genral CP => " + JSON.stringify(restaurant));
  const newTariffSocket = socketTariff(restaurant);
  console.log("new tariffs" + JSON.stringify(newTariffSocket));

  return (
    <SafeArea>
      <RestaurantInfoCard restaurant={restaurant} />
      <MyList3Button
        idField="socketId"
        navigation={navigation}
        onPressDestination="Reserve"
        field3="price"
        filedname3={"Tariff"}
        field1="type"
        filedname1={"Type"}
        filedname2={"Status"}
        field2="status"
        data={newTariffSocket}
        unit1=""
        unit2=""
        unit3="/KW $"
      />
    </SafeArea>
  );
};
