import React from "react";
import Wrapper from "../../assets/wrappers/Job";
import { CPInfoText } from "../../components/CPInfo";
import { Divider } from "@material-ui/core";
import { Link } from "react-router-dom";

const Battery = ({
  cp,
  battery,
  batteryId,
  status,
  maxCapacity,
  minLevel,
  maxLevel,
  percent,
}) => {
  return (
    <Wrapper>
      <header>
        <div className="main-icon">{batteryId}</div>
        <div className="info">
          <h5>Id: {batteryId}</h5>
          <h5>percent: {percent}</h5>
        </div>
      </header>
      <div className="content">
        <div className="content-center">
          <ul>
            <CPInfoText text={`Maximum Capacity: ${maxCapacity}`} />
            <CPInfoText text={`Minimum Level : ${minLevel}`} />
            <CPInfoText text={`Maximum Level: ${maxLevel}`} />
            <CPInfoText text={`Status: ${status}`} />
          </ul>
        </div>
        <footer>
          <Link to="/opt-battery" state={{ battery, cp }}>
            {" "}
            Change Settings{" "}
          </Link>
        </footer>
      </div>
      <Divider style={{ backgroundColor: "#198cb8" }} />
    </Wrapper>
  );
};
export default Battery;
