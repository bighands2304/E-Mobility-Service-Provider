import Offer from "../pages/dashboard/Offer";
import { useSelector, useDispatch } from "react-redux";
import { useEffect } from "react";
import {
  getAllOffers,
  selectOffer,
  setcpId,
} from "../features/offer/offerSlice";
import Wrapper from "../assets/wrappers/Job";

const OffersContainer = ({ cp }) => {
  const { offerlist, isLoading, cpId } = useSelector((store) => store.offers);
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(setcpId(cp.id));
    dispatch(getAllOffers());
  }, []);

  return (
    <Wrapper>
      <h5>Offers for the CP + {cp.name}</h5>
      <div className="offers">
        {offerlist.map((offer) => {
          return <Offer key={offer.id} {...offer} offer={offer} />;
        })}
      </div>
    </Wrapper>
  );
};
export default OffersContainer;
