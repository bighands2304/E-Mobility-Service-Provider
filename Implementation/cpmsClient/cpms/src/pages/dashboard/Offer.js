import React, { useState } from "react";
import Wrapper from "../../assets/wrappers/DashboardFormPage";
import { CPInfoText } from "../../components/CPInfo";
import { Divider, Button } from "@material-ui/core";

const Offer = ({ cp, offer }) => {
  return (
    <Wrapper>
      <header>
        <div className="main-icon">{offer.companyName}</div>
        <div className="info">
          <h5>Id: {offer.offerId}</h5>
          <h5>DSO Provider: {offer.companyName}</h5>
          <h1>DSO id :{offer.dsoId}</h1>
        </div>
      </header>
      <div className="content">
        <div className="content-center">
          <ul>
            <CPInfoText text={`Price: ${offer.price}`} />
            <CPInfoText text={`start : ${offer.availableTimeSlot.startTime}`} />
            <CPInfoText text={`end: ${offer.availableTimeSlot.endTime}`} />

            {offer.availableTimeSlot.isInUse && (
              <>
                <CPInfoText
                  text={`Start Usage: ${offer.usedTimeSlot.startTime}`}
                />
                <CPInfoText
                  text={`End Esage : ${offer.availableTimeSlot.endTime}`}
                />
              </>
            )}
            {!offer.availableTimeSlot.isInUse && (
              <Button variant="contained">ADD</Button>
            )}
          </ul>
        </div>
        <footer></footer>
      </div>
      <Divider style={{ backgroundColor: "#198cb8" }} />
    </Wrapper>
  );
};
export default Offer;
