import { FaLocationArrow, FaBriefcase, FaCalendarAlt } from "react-icons/fa";
import { Link } from "react-router-dom";
import Wrapper from "../assets/wrappers/Job";
import { useDispatch } from "react-redux";
import { JobInfo, JobInfoText } from "./JobInfo";
import moment from "moment";
import { deleteJob, setEditJob } from "../features/job/jobSlice";

import { Divider } from "@material-ui/core";

const Job = ({
  cp,
  cpId,
  name,
  address,
  togglePriceOptimizer,
  toggleEnergyMixOptimizer,
  toggleDSOSelectionOptimizer,
  id,
}) => {
  const dispatch = useDispatch();

  return (
    <Wrapper>
      <header>
        <div className="main-icon">{name}</div>
        <div className="info">
          <h5>{cpId}</h5>
          <h5>{name}</h5>
        </div>
      </header>
      <div className="content">
        <div className="content-center">
          <JobInfo icon={<FaLocationArrow />} text={address} />
          <ul>
            <JobInfoText
              text={`Price Optimizer: ${togglePriceOptimizer ? "On" : "Off"}`}
            />
            <JobInfoText
              text={`Energy Mix Optimizer: ${
                toggleEnergyMixOptimizer ? "On" : "Off"
              }`}
            />
            <JobInfoText
              text={` DSO Selection Optimizer: ${
                toggleDSOSelectionOptimizer ? "On" : "Off"
              }`}
            />
          </ul>
        </div>
        <footer>
          <Link to="/cp-details" state={cp}>
            {" "}
            View Settings
          </Link>
        </footer>
      </div>
      <Divider style={{ backgroundColor: "#198cb8" }} />
    </Wrapper>
  );
};
export default Job;
