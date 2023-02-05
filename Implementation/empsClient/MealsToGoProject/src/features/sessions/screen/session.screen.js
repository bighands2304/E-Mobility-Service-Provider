import { FlatList, View, StyleSheet, Button } from "react-native";
import React, { useContext, useEffect } from "react";
import { SafeArea } from "../../../components/utility/safe-area.component";
import { Text } from "../../../components/typography/text.component";
import { ReservationContext } from "../../../services/reservation/reservation.context";
import { Title } from "react-native-paper";
import {
  RedButton,
  GreenButton,
} from "../../../components/buttons/buttons.component";
import { Spacer } from "../../../components/spacer/spacer.component";

export const SessionsScreen = () => {
  const delay = (ms) => new Promise((resolve) => setTimeout(resolve, ms));

  const fun = async () => {
    await delay(5000);
    RetrieveReservation();
  };

  const {
    reservations,
    doReservationDelete,
    doStartChargingProcess,
    doEndSession,
    RetrieveReservation,
  } = useContext(ReservationContext);

  const sortedReservations = reservations
    .filter((res) => res.type !== "DELETED")
    .sort((a, b) => new Date(b.startTime) - new Date(a.startTime));

  return (
    <SafeArea>
      <FlatList
        data={sortedReservations}
        keyExtractor={(item, index) =>
          Math.floor(Math.random() * 1000000).toString()
        }
        renderItem={({ item }) => (
          <View style={styles2.itemContainer}>
            <View style={styles2.textContainer}>
              <View style={{ flexDirection: "column" }}>
                {item.type === "EXPIRED" && (
                  <>
                    <Text>Date: {item.startTime.split("T")[0]}</Text>
                    <Text>Status: {item.type}</Text>
                  </>
                )}
                {item.type === "ENDED" && (
                  <>
                    <Text>Date: {item.startTime.split("T")[0]}</Text>
                    <Text>Price: {item.price} $</Text>
                    <Text>Status: {item.type}</Text>
                  </>
                )}
                {item.type === "RESERVED" && (
                  <>
                    <Title> Active Rervation:</Title>
                    <Spacer></Spacer>
                    <Spacer></Spacer>
                    <Spacer></Spacer>
                    <Spacer></Spacer>
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
                  </>
                )}
                {item.type === "ACTIVE" && (
                  <>
                    <Title> Active Charging Session:</Title>
                    <Spacer></Spacer>
                    <Spacer></Spacer>
                    <Spacer></Spacer>
                    <Spacer></Spacer>
                    <Text></Text>
                    <Text>Battery Percentage: {item.batteryPercentage}</Text>
                    <Text>Socket: {item.socketId}</Text>
                  </>
                )}
              </View>
            </View>
            <View style={styles2.buttonContainer}>
              {item.type === "RESERVED" && (
                <>
                  <RedButton
                    onPress={() => {
                      doReservationDelete(item.id);
                      fun();
                    }}
                    title={"Delete"}
                  />
                  <GreenButton
                    onPress={() => {
                      doStartChargingProcess(item.id);
                      fun();
                    }}
                    title={"Start Charging Process"}
                  />
                </>
              )}
              {item.type === "ACTIVE" && (
                <RedButton
                  onPress={() => {
                    doEndSession(item.id);
                    fun();
                  }}
                  title={"End Session"}
                />
              )}
            </View>
            <View style={styles2.separator} />
          </View>
        )}
      />
      <View style={styles2.separator} />
    </SafeArea>
  );
};

const styles2 = StyleSheet.create({
  itemContainer: {
    flex: 1,
    flexDirection: "column",
    alignItems: "center",
    padding: 10,
    position: "relative",
  },
  textContainer: {
    flex: 1,
  },
  buttonContainer: {
    position: "relative",
    width: "100%",
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
