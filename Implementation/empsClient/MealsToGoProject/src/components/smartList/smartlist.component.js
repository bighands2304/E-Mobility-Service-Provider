import React from "react";
import { Ionicons } from "@expo/vector-icons";
import { Text } from "../typography/text.component";
import { GreenButton } from "../buttons/buttons.component";
import {
  FlatList,
  TouchableOpacity,
  Image,
  View,
  StyleSheet,
} from "react-native";
//<Image source={item.image} style={styles.image} />
//    <View style={styles.textContainer}></View>

export const MyList3 = ({
  navigation,
  onPressDestination,
  field1,
  field2,
  field3,
  filedname1,
  filedname2,
  filedname3,
  data,
  sortByFavourite,
  unit1,
  unit2,
  unit3,
}) => {
  let sortedData;

  if (sortByFavourite) {
    sortedData = data.sort((a, b) => {
      if (a[sortByFavourite] === b[sortByFavourite]) return 0;
      if (a[sortByFavourite]) return -1;
      return 1;
    });
  } else {
    sortedData = data;
  }

  return (
    <FlatList
      data={sortedData}
      renderItem={({ item }) => (
        <TouchableOpacity
          onPress={() =>
            navigation.navigate(onPressDestination, {
              item,
            })
          }
        >
          <View style={styles.itemContainer}>
            <View style={styles.textContainer}>
              <Text>
                {filedname1} : {item[field1]} {unit1}
              </Text>
              <Text>
                {filedname2} : {item[field2]} {unit2}
              </Text>
              <Text>
                {filedname3} : {item[field3]} {unit3}
              </Text>
            </View>
            {item[sortByFavourite] && (
              <Ionicons
                name="md-star"
                size={25}
                color="#00C853"
                style={styles.icon}
              />
            )}
          </View>
        </TouchableOpacity>
      )}
      keyExtractor={(item) => item[field3]}
      ItemSeparatorComponent={() => <View style={styles.separator} />}
    />
  );
};

const styles = StyleSheet.create({
  itemContainer: {
    flex: 1,
    flexDirection: "row",
    alignItems: "center",
    padding: 10,
    position: "relative",
  },
  separator: {
    height: 1,
    width: "100%",
    backgroundColor: "#ccc",
    marginVertical: 10,
  },
  icon: {
    position: "absolute",
    right: 10,
    top: "50%",
  },
});

export const MyList3Button = ({
  navigation,
  onPressDestination,
  idField,
  field1,
  field2,
  field3,
  filedname1,
  filedname2,
  filedname3,
  data,
  sortByFavourite,
  unit1,
  unit2,
  unit3,
  onSubmit,
  p1spec,
  p2gen,
}) => {
  let sortedData;

  if (sortByFavourite) {
    sortedData = data.sort((a, b) => {
      if (a[sortByFavourite] === b[sortByFavourite]) return 0;
      if (a[sortByFavourite]) return -1;
      return 1;
    });
  } else {
    sortedData = data;
  }

  return (
    <FlatList
      data={sortedData}
      renderItem={({ item }) => (
        <View style={styles2.itemContainer}>
          <View style={styles2.textContainer}>
            <View style={{ flexDirection: "column" }}>
              <Text>
                {filedname1} : {item[field1]} {unit1}
              </Text>
              <Text>
                {filedname2} : {item[field2]} {unit2}
              </Text>
              <Text>
                {filedname3} : {item[field3]} {unit3}
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
