import React, { useContext, useState } from "react";
import { ScrollView } from "react-native";
import { List } from "react-native-paper";
import { MyList3Button } from "../../../components/smartList/smartlist.component";

import { RestaurantInfoCard } from "../component/restaurant-info-card.component";

import { SafeArea } from "../../../components/utility/safe-area.component";
import { socketTariff } from "../../../components/utility/socketTariffextractor";
import { ReservationContext } from "../../../services/reservation/reservation.context";

export const RestaurantDetailScreen = ({ navigation, route }) => {
  const { doReservation } = useContext(ReservationContext);

  const { restaurant } = route.params;

  const newTariffSocket = socketTariff(restaurant);

  return (
    <SafeArea>
      <RestaurantInfoCard restaurant={restaurant} />
      <MyList3Button
        onSubmit={doReservation}
        p1spec="socketId"
        p2gen={restaurant.cpId}
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
