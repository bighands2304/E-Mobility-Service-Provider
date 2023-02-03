import Wrapper from "../../assets/wrappers/Job";
import { JobInfoText } from "../../components/JobInfo";
import { Divider } from "@material-ui/core";
const Tariff = ({
  tariff,
  tariffId,
  socketType,
  startDate,
  endDate,
  price,
  stepSize,
}) => {
  return (
    <Wrapper>
      <header>
        <div className="main-icon">{tariffId}</div>
        <div className="info">
          <h5>Id: {tariffId}</h5>
          <h5>Socket Type: {socketType}</h5>
        </div>
      </header>
      <div className="content">
        <div className="content-center">
          <ul>
            <JobInfoText text={`Price: ${price}`} />
            <JobInfoText text={`start : ${startDate}`} />
            <JobInfoText text={`end: ${endDate}`} />
            <JobInfoText text={`Prstep Size: ${stepSize}`} />
          </ul>
        </div>
        <footer></footer>
      </div>
      <Divider style={{ backgroundColor: "#198cb8" }} />
    </Wrapper>
  );
};
export default Tariff;
