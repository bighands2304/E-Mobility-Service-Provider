import React from "react";

import styled from "styled-components/native";

const FavouritesWrapper = styled.View`
  padding: 10px;
`;
export const FavouritesBar = ({ favourites, onNavigate }) => {
  if (!favourites.length) {
    return null;
  }
  return <FavouritesWrapper></FavouritesWrapper>;
};
