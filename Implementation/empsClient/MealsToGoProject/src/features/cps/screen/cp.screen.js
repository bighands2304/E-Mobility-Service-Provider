import React, { useContext, useState } from "react";
import { ActivityIndicator } from "react-native-paper";
import { TouchableOpacity } from "react-native";
import styled from "styled-components/native";
import { Spacer } from "../../../components/spacer/spacer.component";
import { SafeArea } from "../../../components/utility/safe-area.component";
import { CPsContext } from "../../../services/cps/cps.context";
import { FavouritesContext } from "../../../services/favourites/favourites.context";
import { FadeInView } from "../../../components/animations/fade.animation";
import { Search } from "../component/search.component";
import { CPInfoCard } from "../component/cp-info-card.component";
import { FavouritesBar } from "../../../components/favorites/favourites-bar.component";
import { CPList } from "../component/cp-list,styles";

const Loading = styled(ActivityIndicator)`
  margin-left: -25px;
`;
const LoadingContainer = styled.View`
  position: absolute;
  top: 50%;
  left: 50%;
`;
export const CPScreen = ({ navigation }) => {
  const { isLoading, cps } = useContext(CPsContext);
  const [isToggled, setIsToggled] = useState(false);
  const { favourites } = useContext(FavouritesContext);

  return (
    <SafeArea>
      {isLoading && (
        <LoadingContainer>
          <Loading size={50} animating={true} color="#e37f43" />
        </LoadingContainer>
      )}
      <Search
        isFavouritesToggled={isToggled}
        onFavouritesToggle={() => setIsToggled(!isToggled)}
      />
      {isToggled && (
        <FavouritesBar
          favourites={favourites}
          onNavigate={navigation.navigate}
        />
      )}
      <CPList
        data={cps}
        renderItem={({ item }) => {
          return (
            <TouchableOpacity
              onPress={() =>
                navigation.navigate("CPDetail", {
                  cp: item,
                })
              }
            >
              <Spacer position="bottom" size="large">
                <FadeInView>
                  <CPInfoCard cp={item} />
                </FadeInView>
              </Spacer>
            </TouchableOpacity>
          );
        }}
        keyExtractor={(item) => item.name}
      />
    </SafeArea>
  );
};
