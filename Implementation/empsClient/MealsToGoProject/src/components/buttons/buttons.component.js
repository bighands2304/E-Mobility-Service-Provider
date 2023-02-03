import Rect from "react";

import styled, { useTheme } from "styled-components/native";
import { StyleSheet, Text, TouchableOpacity, View } from "react-native";
import { Spacer } from "../spacer/spacer.component";

const colorVariant = {
  green: "#00C853",
  black: "#000000",
  red: " #F44336",
  white: "#FFFFFF",
  greydark: "#333333",
  greylight: "#BBBBBB",
};

const styles = StyleSheet.create({
  buttonContainer: {
    justifyContent: "center",
    alignSelef: "stretch",
    backgroundColor: "#F44336",
    borderRadius: 5,
    padding: 20,
    margin: 10,
  },
  buttonText: {
    color: "#FFFFFF",
    fontWeight: "bold",
    textAlign: "center",
  },
});

export const RedButton = ({ onPress, title }) => (
  <TouchableOpacity onPress={onPress} style={styles.buttonContainer}>
    <Text style={styles.buttonText}>{title}</Text>
  </TouchableOpacity>
);

export const GreenButton = ({ onPress, title }) => (
  <TouchableOpacity
    onPress={onPress}
    style={[styles.buttonContainer, { backgroundColor: "#00C853" }]}
  >
    <Text style={styles.buttonText}>{title}</Text>
  </TouchableOpacity>
);

export const YellowButton = ({ onPress, title }) => (
  <TouchableOpacity
    onPress={onPress}
    style={[styles.buttonContainer, { backgroundColor: "#FDB813" }]}
  >
    <Text style={styles.buttonText}>{title}</Text>
  </TouchableOpacity>
);

export const DarkButton = ({ onPress, title }) => (
  <TouchableOpacity
    onPress={onPress}
    style={[styles.buttonContainer, { backgroundColor: "#000000" }]}
  >
    <Text style={styles.buttonText}>{title}</Text>
  </TouchableOpacity>
);
