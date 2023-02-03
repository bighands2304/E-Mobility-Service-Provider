/*
import { useEffect } from "react";
import Job from "./Job";
import Wrapper from "../assets/wrappers/JobsContainer";
import { useSelector, useDispatch } from "react-redux";
import Loading from "./Loading";
import { getAllJobs } from "../features/allJobs/allJobsSlice";
import PageBtnContainer from "./PageBtnContainer";
const JobsContainer = () => {
  const {
    jobs = [],
    isLoading,
    page,
    totalJobs,
    numOfPages,
  } = useSelector((store) => store.allJobs);
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(getAllJobs());
  }, [page]);

  if (isLoading) {
    return <Loading />;
  }

  if (jobs.length === 0) {
    return (
      <Wrapper>
        <h2>No jobs to display...</h2>
      </Wrapper>
    );
  }

  return (
    <Wrapper>
      <h5>
        {totalJobs} job{jobs.length > 1 && "s"} found
      </h5>
      <div className="jobs">
        {jobs.map((job) => {
          return <Job key={job._id} {...job} />;
        })}
      </div>
      {numOfPages > 1 && <PageBtnContainer />}
    </Wrapper>
  );
};
export default JobsContainer;
*/

import { useEffect } from "react";
import Job from "./Job";
import Wrapper from "../assets/wrappers/Job";
import { useSelector, useDispatch } from "react-redux";
import Loading from "./Loading";
import { getAllJobs } from "../features/allJobs/allJobsSlice";
import PageBtnContainer from "./PageBtnContainer";
const JobsContainer = () => {
  const { cps, isLoading, page, totalCPs, numOfPages } = useSelector(
    (store) => store.allJobs
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
          return <Job key={cp.id} {...cp} cp={cp} />;
        })}
      </div>
    </Wrapper>
  );
};
export default JobsContainer;
