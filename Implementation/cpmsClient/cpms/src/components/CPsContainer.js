import { useEffect } from "react";
import CP from "./CP";
import Wrapper from "../assets/wrappers/Job";
import { useSelector, useDispatch } from "react-redux";
import Loading from "./Loading";
import { getAllJobs } from "../features/allCPs/allCPsSlice";
import PageBtnContainer from "./PageBtnContainer";
const CPsContainer = () => {
  const { cps, isLoading, page, totalCPs, numOfPages } = useSelector(
    (store) => store.allCPs
  );
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(getAllJobs());
  }, [page]);

  //  console.log("DATA DATA DATA ==> " + data);

  useEffect(() => {
    dispatch(getAllJobs());
  }, []);
  if (isLoading) return <div>Loading...</div>;

  return (
    <Wrapper>
      <h5>{totalCPs} Cps found</h5>
      <div className="jobs">
        {cps.map((cp) => {
          return <CP key={cp.id} {...cp} cp={cp} />;
        })}
      </div>
    </Wrapper>
  );
};
export default CPsContainer;
