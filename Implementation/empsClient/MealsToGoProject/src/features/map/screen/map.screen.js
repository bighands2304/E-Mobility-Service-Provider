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
  const { favVehicle } = useContext(FavouritesContext);
  const [latDelta, setLatDelta] = useState(0);
  let socketTypeFav = "FAST";
  if (favVehicle) {
    socketTypeFav = favVehicle[0].SocketType;
  }

  //console.log("fav =======>=>=>" + JSON.stringify(socketTypeFav));
  //console.log("cps =======>=>=>" + JSON.stringify(restaurants));
  const colorDarkMap = "#000000";
  const colorRedMap = "#CF1827";
  const colorGreenMap = "#0064ff";
  console.log("location structure " + JSON.stringify(location));

  useEffect(() => {
    const northeastLat = location.result.viewport.northeast.lat;
    const southwestLat = location.result.viewport.southwest.lat;
    setLatDelta(northeastLat - southwestLat);
  }, [location]);
  console.log("location" + location);
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
              restaurant.sockets[i].availability === "available" &&
              restaurant.sockets[i].status === "free"
            ) {
              pinColor = colorGreenMap;
              break;
            } else if (
              restaurant.sockets[i].type === socketTypeFav &&
              restaurant.sockets[i].availability === "available"
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
