import OffersContainer from "../../components/OffersContainers";
import { Link, useLocation } from "react-router-dom";

const AllOffers = () => {
  const location = useLocation();
  const cp = location.state;
  return <OffersContainer cp={cp} />;
};
export default AllOffers;
