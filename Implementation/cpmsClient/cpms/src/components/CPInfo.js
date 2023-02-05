import Wrapper from "../assets/wrappers/JobInfo";

export const CPInfo = ({ icon, text }) => {
  return (
    <Wrapper>
      <span className="icon">{icon} </span>
      <span className="text">{text} </span>
    </Wrapper>
  );
};

export const CPInfoText = ({ text }) => {
  return (
    <Wrapper>
      <span className="text">{text} </span>
    </Wrapper>
  );
};
