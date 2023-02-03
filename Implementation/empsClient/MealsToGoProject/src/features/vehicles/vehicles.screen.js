import React, { useContext, useState } from "react";
import { Text } from "../../components/typography/text.component";
import { SafeArea } from "../../components/utility/safe-area.component";
import { GreenButton } from "../../components/buttons/buttons.component";
import { VehicleContext } from "../../services/vehicles/mock/vehcile.context";
import { Spacer } from "../../components/spacer/spacer.component";
import { FlatList } from "react-native";
import { View } from "react-native";
import { MyList3 } from "../../components/smartList/smartlist.component";

export const VehiclesScreen = ({ navigation }) => {
  const { vehicles } = useContext(VehicleContext);
  //console.log(vehicles);
  //("data===>" + JSON.stringify(vehicles));
  return (
    <SafeArea>
      <Text variant="title">Your Vehicles:</Text>

      <MyList3
        navigation={navigation}
        onPressDestination="VehicleDetail"
        field3="Battery"
        filedname3={"Battery"}
        field1="VehicleModel"
        filedname1={"Model"}
        filedname2={"Range"}
        field2="Range"
        data={vehicles}
        sortByFavourite="isFavourite"
        unit1=""
        unit2="km"
        unit3="%"
      />

      <GreenButton
        title="Add new vehicle"
        onPress={() => navigation.navigate("AddVehicle")}
      />
    </SafeArea>
  );
};
