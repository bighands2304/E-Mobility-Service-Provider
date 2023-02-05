import React, { useContext, useState, useEffect } from "react";
import { LocationContext } from "../../../services/location/location.context";
import MapView from "react-native-maps";
import { View } from "react-native";
import styled from "styled-components/native";
import { CPsContext } from "../../../services/cps/cps.context";
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
  const { cps } = useContext(CPsContext);
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
        {cps.map((cp) => {
          let pinColor = colorDarkMap;
          console.log("AAAA");

          console.log("AAAA");
          console.log("AAAA");
          console.log("AAAA");
          console.log("AAAA");
          console.log("AAAA");
          console.log("AAAA");
          console.log(JSON.stringify(cp));

          for (let i = 0; i < cp.sockets.length; i++) {
            if (
              cp.sockets[i].type === socketTypeFav &&
              cp.sockets[i].availability === "AVAILABLE" &&
              cp.sockets[i].status === "AVAILABLE"
            ) {
              pinColor = colorGreenMap;
              break;
            } else if (
              cp.sockets[i].type === socketTypeFav &&
              cp.sockets[i].availability === "AVAILABLE"
            ) {
              pinColor = colorRedMap;
            }
          }
          return (
            <Marker
              key={cp.name}
              title={cp.name}
              pinColor={pinColor}
              coordinate={{
                latitude: cp.latitude,
                longitude: cp.longitude,
              }}
            >
              <Callout
                onPress={() =>
                  navigation.navigate("CPDetail", {
                    cp,
                  })
                }
              >
                <MapCallout cp={cp} />
              </Callout>
            </Marker>
          );
        })}
      </Map>
    </>
  );
};
