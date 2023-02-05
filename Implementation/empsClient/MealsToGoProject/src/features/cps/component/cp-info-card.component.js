import React from "react";
import { Text } from "../../../components/typography/text.component";
import { View } from "react-native";
import { Favourite } from "../../../components/favorites/favourite.component";

import {
  CPCard,
  CPCardCover,
  Info,
  Icon,
  Address,
} from "./cp.info-card.styles";

export const CPInfoCard = ({ cp = {} }) => {
  console.log("dlcjndsknvs");
  console.log("cp" + cp);

  console.log("cp in cp info card" + JSON.stringify(cp));
  const offerIcon =
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSySiFKb2TXagm7fozKF74N8HCYLZaQyradQQ&usqp=CAU";
  // to do : false is closed.

  return (
    <CPCard elevation={5}>
      <View>
        <Favourite cp={cp} />
        <CPCardCover key={cp.name} source={{ uri: cp.photo[0] }} />
      </View>
      <Info>
        <View style={{ position: "relative" }}>
          <Text variant="label">{cp.name} </Text>
          {cp.tariffs.some((tariff) => tariff.specialOffer) && (
            <Icon
              source={{ uri: offerIcon }}
              style={{ position: "absolute", right: 0, width: 25, height: 25 }}
            />
          )}
        </View>

        <Address>{cp.address}</Address>
      </Info>
    </CPCard>
  );
};
