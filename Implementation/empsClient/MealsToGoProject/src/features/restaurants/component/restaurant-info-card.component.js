import React from "react";
import { Text } from "../../../components/typography/text.component";
import { Spacer } from "../../../components/spacer/spacer.component";
import { SvgXml } from "react-native-svg";
import { View } from "react-native";
import { Favourite } from "../../../components/favorites/favourite.component";
import star from "../../../assets/star";
import open from "../../../assets/open";

import {
  RestaurantCard,
  RestaurantCardCover,
  Info,
  Section,
  SectionEnd,
  Rating,
  Icon,
  Address,
} from "../component/restaurant.info-card.styles";

export const RestaurantInfoCard = ({ restaurant = {} }) => {
  const offerIcon =
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSySiFKb2TXagm7fozKF74N8HCYLZaQyradQQ&usqp=CAU";
  // to do : false is closed.
  return (
    <RestaurantCard elevation={5}>
      <View>
        <Favourite restaurant={restaurant} />
        <RestaurantCardCover
          key={restaurant.name}
          source={{ uri: restaurant.photo[0] }}
        />
      </View>
      <Info>
        <View style={{ position: "relative" }}>
          <Text variant="label">{restaurant.name} </Text>
          {restaurant.tariffs.some((tariff) => tariff.specialOffer) && (
            <Icon
              source={{ uri: offerIcon }}
              style={{ position: "absolute", right: 0, width: 25, height: 25 }}
            />
          )}
        </View>

        <Address>{restaurant.address}</Address>
      </Info>
    </RestaurantCard>
  );
};
