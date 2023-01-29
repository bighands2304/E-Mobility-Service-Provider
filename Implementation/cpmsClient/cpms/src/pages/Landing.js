import logo3 from "../assets/images/logo3.svg";
import styled from "styled-components";
import back1 from "../assets/images/back1.svg";

const Landing = () => {
  return (
    <Wrapper>
      <nav>
        <img
          src={logo3}
          alt="E-MALL logo"
          className="e-mall"
          style={{ width: "100px", height: "100px" }}
        />
      </nav>
      <div className="container page">
        {/* info */}
        <div className="info">
          <h1>
            E-MALL <span>CPMS</span>
          </h1>
          <p>Smart Charging Point Manager for E-MALL</p>
          <button className="btn btn-hero">Login/Register</button>
        </div>
        <img src={back1} alt="job hunt" className="img main-img" />
      </div>
    </Wrapper>
  );
};
const Wrapper = styled.main`
  nav {
    width: var(--fluid-width);
    max-width: var(--max-width);
    margin: 0 auto;
    height: var(--nav-height);
    display: flex;
    align-items: center;
  }
  .page {
    min-height: calc(100vh - var(--nav-height));
    display: grid;
    align-items: center;
    margin-top: -3rem;
  }
  h1 {
    font-weight: 700;
    span {
      color: var(--primary-500);
    }
  }
  p {
    color: var(--grey-600);
  }
  .main-img {
    display: none;
  }
  @media (min-width: 992px) {
    .page {
      grid-template-columns: 1fr 1fr;
      column-gap: 3rem;
    }
    .main-img {
      display: block;
    }
  }
`;
export default Landing;
