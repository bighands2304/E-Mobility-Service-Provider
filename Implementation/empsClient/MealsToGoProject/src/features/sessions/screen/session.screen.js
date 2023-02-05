import { FlatList, View, StyleSheet, Button } from "react-native";
import React, { useContext } from "react";
import { SafeArea } from "../../../components/utility/safe-area.component";
import { Text } from "../../../components/typography/text.component";
import { ReservationContext } from "../../../services/reservation/reservation.context";
import { Title } from "react-native-paper";

export const SessionsScreen = () => {
  const {
    reservations,
    doReservationDelete,
    doStartChargingProcess,
    doEndSession,
  } = useContext(ReservationContext);

  /*
  const sortedReservations = reservations
    .filter((res) => res.type !== "DELETED")
    .sort((a, b) => new Date(b.startTime) - new Date(a.startTime));
    */

  return (
    <SafeArea>
      <Text>Sessions</Text>
      <FlatList
        data={reservations}
        keyExtractor={(item, index) =>
          Math.floor(Math.random() * 1000000).toString()
        }
        renderItem={({ item }) => (
          <View>
            {item.type === "EXPIRED" && (
              <View>
                <Text>Data: {item.startTime.split("T")[0]}</Text>
                <Text>Status: {item.type}</Text>
              </View>
            )}
            {item.type === "ENDED" && (
              <View>
                <Text>Data: {item.startTime.split("T")[0]}</Text>
                <Text>Price: {item.price}</Text>
              </View>
            )}
            {item.type === "RESERVED" && (
              <View>
                <Text>
                  Remaining time:{" "}
                  {Math.floor(
                    (new Date(item.expiryDate).getTime() -
                      new Date().getTime()) /
                      60000
                  )}{" "}
                  minutes
                </Text>
                <Text>Address: {item.cp.address}</Text>
                <Text>CP: {item.cp.name}</Text>
                <Text>Socket Number: {item.socketId}</Text>
                <View
                  style={{
                    flexDirection: "row",
                    justifyContent: "space-between",
                  }}
                >
                  <Button
                    title="Delete"
                    color="red"
                    onPress={() => doReservationDelete(item.id)}
                  />
                  <Button
                    title="Start Charging Process"
                    color="green"
                    onPress={() => doStartChargingProcess(item.id)}
                  />
                </View>
              </View>
            )}
            {item.type === "ACTIVE" && (
              <View>
                <Text>Battery Percentage: {item.batteryPercentage}</Text>
                <Text>Socket: {item.socketId}</Text>
                <Button
                  title="End Session"
                  color="red"
                  onPress={() => doEndSession(item.id)}
                />
              </View>
            )}
          </View>
        )}
      />
    </SafeArea>
  );
};
const styles2 = StyleSheet.create({
  itemContainer: {
    flex: 1,
    flexDirection: "row",
    alignItems: "center",
    padding: 10,
    position: "relative",
  },
  textContainer: {
    flex: 1,
  },
  buttonContainer: {
    position: "absolute",
    right: 0,
  },
  separator: {
    height: 1,
    width: "100%",
    backgroundColor: "#ccc",
    marginVertical: 10,
  },
});

/*
<SafeArea>
      <Text variant="title"> Sessions: </Text>
      <FlatList
        data={sortedReservations}
        renderItem={({ item }) => (
          <View style={styles2.itemContainer}>
            <View style={styles2.textContainer}>
              <View style={{ flexDirection: "column" }}>
                <Text>

                </Text>
                <Text>
              
                </Text>
                <Text>
               
                </Text>
                {item[field2] === "AVAILABLE" && (
                  <View style={styles2.buttonContainer}>
                    <GreenButton
                      onPress={() => onSubmit(item[p1spec], p2gen)}
                      title={"Reserve"}
                    />
                  </View>
                )}
              </View>
            </View>
          </View>
        )}
        keyExtractor={(item) => item[idField]}
        ItemSeparatorComponent={() => <View style={styles.separator} />}
      />
    </SafeArea>

*/
