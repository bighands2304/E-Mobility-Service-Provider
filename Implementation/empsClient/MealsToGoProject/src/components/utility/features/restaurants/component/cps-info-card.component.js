import React from "react";
import { Text } from "../../../components/typography/text.component";
import { Spacer } from "../../../components/spacer/spacer.component";
import { SvgXml } from "react-native-svg";
import { View } from "react-native";
import { Favourite } from "../../../components/favorites/favourite.component";

import {
  RestaurantCard,
  RestaurantCardCover,
  Info,
  Icon,
  Address,
} from "./restaurant.info-card.styles";

export const CPInfoCard = ({ cp = {} }) => {
  const offerIcon =
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSySiFKb2TXagm7fozKF74N8HCYLZaQyradQQ&usqp=CAU";
  // to do : false is closed.

  console.log("cp in cp info card" + JSON.stringify(cp));
  console.log("sindsfus");
  return (
    <RestaurantCard elevation={5}>
      <View>
        <Favourite cp={cp} />
        <RestaurantCardCover key={cp.name} source={{ uri: cp.photo[0] }} />
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
    </RestaurantCard>
  );
};
