import { Logo } from "../components";
import back1 from "../assets/images/back1.svg";
import Wrapper from "../assets/wrappers/LandingPage";
import { Link } from "react-router-dom";

const Landing = () => {
  return (
    <Wrapper>
      <nav>
        <Logo />
      </nav>
      <div className="container page">
        {/* info */}
        <div className="info">
          <h1>
            E-MALL <span>CPMS</span>
          </h1>
          <p>Smart Charging Point Manager for E-MALL</p>
          <Link to="/register" className="btn btn-hero">
            Login/Register
          </Link>
        </div>
        <img src={back1} alt="job hunt" className="img main-img" />
      </div>
    </Wrapper>
  );
};

export default Landing;
