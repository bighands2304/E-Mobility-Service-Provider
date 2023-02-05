import customFetch from "../utils/axios";
import Offer from "../pages/dashboard/Offer";
import { useSelector, useDispatch } from "react-redux";
import { useEffect, useState } from "react";
import {
  getAllOffers,
  selectOffer,
  setcpId,
} from "../features/offer/offerSlice";
import Wrapper from "../assets/wrappers/Job";

const OffersContainer = ({ cp }) => {
  const dispatch = useDispatch();
  const [offerList2, setOfferList] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      const url = `/chargingPoints/${cp.id}/dso/offers`;
      const resp = await customFetch.get(url);
      console.log(resp.data);
      setOfferList(resp.data);
    };
    fetchData();
  }, []);

  console.log(offerList2);
  if (!offerList2) {
    return (
      <Wrapper>
        <h1>no offer for the cp {cp.name}</h1>
      </Wrapper>
    );
  }
  return (
    <Wrapper>
      <h5>Offers for the CP + {cp.name}</h5>
      <div className="offers">
        {offerList2.map((offer) => {
          return <Offer key={offer.id} {...offer} offer={offer} />;
        })}
      </div>
    </Wrapper>
  );
};
export default OffersContainer;
