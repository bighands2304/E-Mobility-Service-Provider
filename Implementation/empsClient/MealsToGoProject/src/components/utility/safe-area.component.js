import { StatusBar, SafeAreaView } from "react-native";
import styled from "styled-components/native";

export const SafeArea = styled(SafeAreaView)`
  flex: 1;
  align-items: stretch;
  display: flex;

  ${StatusBar.currentHeight && `margin-top: ${StatusBar.currentHeight}px`};
`;
