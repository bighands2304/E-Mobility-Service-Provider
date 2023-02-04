import React, { useContext } from "react";
import { SafeArea } from "../../components/utility/safe-area.component";
import { Text } from "../../components/typography/text.component";
import { useNavigation } from "@react-navigation/native";
import { StyleSheet, View } from "react-native";
import { VehicleContext } from "../../services/vehicles/mock/vehcile.context";

import {
  RedButton,
  GreenButton,
  DarkButton,
} from "../../components/buttons/buttons.component";

export const VehicleDetailScreen = ({ route }) => {
  const { DeleteVehcile } = useContext(VehicleContext);

  const { item } = route.params;
  const navigation = useNavigation();
  return (
    <SafeArea>
      <Text variant="title">{item.VehicleModel}</Text>
      <View style={styles.listContainer}>
        <Text style={styles.listItem}>VinCode: {item.VinCode}</Text>
        <View style={styles.separator} />
        <Text style={styles.listItem}>SocketType: {item.SocketType}</Text>
        <View style={styles.separator} />
        <Text style={styles.listItem}>
          Range: {item.Range} {"Km"}
        </Text>
        <View style={styles.separator} />
        <Text style={styles.listItem}>
          Battery: {item.Battery} {"%"}
        </Text>
        <View style={styles.separator} />
      </View>
      <View style={styles.separator2}></View>
      <View style={styles.buttonContainer}>
        <GreenButton
          title="Find a CP"
          onPress={() => {
            navigation.navigate("VehiclesMain");
          }}
        />

        <RedButton
          title="Remove Vehicle"
          // dummy
          onPress={() => DeleteVehcile(item.VinCode)}
        />
      </View>
    </SafeArea>
  );
};

const styles = StyleSheet.create({
  listContainer: {
    marginTop: 10,
  },
  listItem: {
    fontSize: 16,
    marginVertical: 5,
    padding: 25,
    backgroundColor: "#fff",
    borderBottomWidth: 1,
    borderBottomColor: "#ccc",
  },
  separator: {
    height: 1,
    width: "100%",
    backgroundColor: "#ccc",
    marginVertical: 5,
  },
  buttonContainer: {
    flexDirection: "column",
    justifyContent: "stretch",
    alignItems: "stretch",
    marginTop: 10,
    paddingHorizontal: 10,
  },
  separator2: {
    flex: 1,
  },
});
