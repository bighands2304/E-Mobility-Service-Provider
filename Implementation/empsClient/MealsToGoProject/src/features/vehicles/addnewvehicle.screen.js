import React, { useContext, useState } from "react";
import { SafeArea } from "../../components/utility/safe-area.component";
import { Text } from "../../components/typography/text.component";
import { GreenButton } from "../../components/buttons/buttons.component";
import { VehicleContext } from "../../services/vehicles/mock/vehcile.context";

import { StyleSheet, TextInput, View } from "react-native";

export const AddNewVehicle = ({ navigation }) => {
  const [vehicleName, setVehicleName] = useState("");
  const [vehicleCode, setVehicleCode] = useState("");
  const { isLoading, AddNewVehicle } = useContext(VehicleContext);

  return (
    <SafeArea>
      <Text variant="title">Add Vehicle </Text>
      <View style={styles.container}>
        <View style={styles.inputContainer}>
          <Text style={styles.label}>Vin Code:</Text>
          <TextInput
            style={styles.input}
            value={vehicleCode}
            onChangeText={(text) => setVehicleCode(text)}
          />
        </View>
      </View>
      <GreenButton
        title="Submit"
        onPress={() => {
          AddNewVehicle(vehicleCode);
        }}
      />
    </SafeArea>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
  },
  input: {
    borderWidth: 1,
    borderColor: "#ccc",
    padding: 10,
    marginBottom: 10,
    borderRadius: 5,
  },
  inputContainer: {
    marginBottom: 20,
  },
  label: {
    fontWeight: "bold",
    marginBottom: 5,
  },
});
