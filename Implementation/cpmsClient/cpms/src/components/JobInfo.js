import Wrapper from "../assets/wrappers/JobInfo";

export const JobInfo = ({ icon, text }) => {
  return (
    <Wrapper>
      <span className="icon">{icon} </span>
      <span className="text">{text} </span>
    </Wrapper>
  );
};

export const JobInfoText = ({ text }) => {
  return (
    <Wrapper>
      <span className="text">{text} </span>
    </Wrapper>
  );
};
