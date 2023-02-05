import React from "react";
import styled from "styled-components/native";
import WebView from "react-native-webview";
import { Platform } from "react-native";

import { Text } from "../typography/text.component";

const CompactImage = styled.Image`
  border-radius: 10px;
  width: 120px;
  height: 100px;
`;

const CompactWebview = styled(WebView)`
  border-radius: 10px;
  width: 120px;
  height: 100px;
`;

const Item = styled.View`
  padding: 10px;
  max-width: 120px;
  align-items: center;
`;

const isAndroid = Platform.OS === "android";

export const CPInfo = ({ cp, isMap }) => {
  const Image = isAndroid && isMap ? CompactWebview : CompactImage;

  console.log("cp in cp info card" + JSON.stringify(cp));
  console.log("lfsekmfsl");

  return (
    <Item>
      <Image source={{ uri: cp.photo[0] }} />
      <Text center variant="label" numberOfLines={3}>
        {cp.name}
      </Text>
      <Text center variant="caption" numberOfLines={3}>
        {cp.address}
      </Text>
    </Item>
  );
};
