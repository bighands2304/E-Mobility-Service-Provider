import React, { useContext, useState, useEffect } from "react";
import { LocationContext } from "../../../services/location/location.context";
import MapView from "react-native-maps";
import { View } from "react-native";
import styled from "styled-components/native";
import { RestaurantsContext } from "../../../services/restaurants/restaurants.context";
import { Search } from "../components/search.component";
import { Marker, Callout } from "react-native-maps";
import { FavouritesContext } from "../../../services/favourites/favourites.context";
import { MapCallout } from "../components/map-collout.component";

const Map = styled(MapView)`
  height: 100%;
  width: 100%;
`;
export const MapScreen = ({ navigation }) => {
  const { location } = useContext(LocationContext);
  const { restaurants = [] } = useContext(RestaurantsContext);
  const { socketF, favourite } = useContext(FavouritesContext);
  const [latDelta, setLatDelta] = useState(0);
  let socketTypeFav = "FAST";
  if (favourite) {
    socketTypeFav = socketF;
  }

  const colorDarkMap = "#000000";
  const colorRedMap = "#CF1827";
  const colorGreenMap = "#0064ff";

  useEffect(() => {
    const northeastLat = location.result.viewport.northeast.lat;
    const southwestLat = location.result.viewport.southwest.lat;
    setLatDelta(northeastLat - southwestLat);
  }, [location]);

  return (
    <>
      <Search />
      <Map
        region={{
          latitude: location.result.lat,
          longitude: location.result.lng,
          latitudeDelta: latDelta,
          longitudeDelta: 0.02,
        }}
      >
        {restaurants.map((restaurant) => {
          let pinColor = colorDarkMap;

          for (let i = 0; i < restaurant.sockets.length; i++) {
            if (
              restaurant.sockets[i].type === socketTypeFav &&
              restaurant.sockets[i].availability === "AVAILABLE" &&
              restaurant.sockets[i].status === "AVAILABLE"
            ) {
              pinColor = colorGreenMap;
              break;
            } else if (
              restaurant.sockets[i].type === socketTypeFav &&
              restaurant.sockets[i].availability === "AVAILABLE"
            ) {
              pinColor = colorRedMap;
            }
          }
          return (
            <Marker
              key={restaurant.name}
              title={restaurant.name}
              pinColor={pinColor}
              coordinate={{
                latitude: restaurant.latitude,
                longitude: restaurant.longitude,
              }}
            >
              <Callout
                onPress={() =>
                  navigation.navigate("RestaurantDetail", {
                    restaurant,
                  })
                }
              >
                <MapCallout restaurant={restaurant} />
              </Callout>
            </Marker>
          );
        })}
      </Map>
    </>
  );
};
