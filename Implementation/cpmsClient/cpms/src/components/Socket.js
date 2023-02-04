import React, { useState } from "react";
import { createAsyncThunk } from "@reduxjs/toolkit";
import { useDispatch } from "react-redux";
import Wrapper from "../assets/wrappers/Job";
import { JobInfoText } from "./JobInfo";
import Switch from "react-switch";
import { Divider } from "@material-ui/core";
import { toast } from "react-toastify";
import { changeAvailability } from "../features/socket/socketThunk";

const Socket = ({ cp, socket, id, socketId, type, status, availability }) => {
  const [isAvailable, setIsAvailable] = useState(availability === "AVAILABLE");

  const dispatch = useDispatch();

  const changeAvailabilityHelper = createAsyncThunk(
    `/chargingPoints/${cp.id}/sockets/${socketId}`,
    async (data, thunkAPI) => {
      const url = `/chargingPoints/${cp.id}/sockets/${socketId}`;
      changeAvailability(data, url, thunkAPI).then((resp) => {
        if ((resp.status === 200) | (resp.status === 201)) {
          toast.success("Success");
          setIsAvailable(!isAvailable);
        } else {
          toast.error("Error");
        }
      });
    }
  );
  const handleAvailabilityUpdate = async () => {
    const date = new Date();
    date.setMinutes(date.getMinutes() + 1);
    const dateString = date.toISOString();
    let newAv = true;

    if (isAvailable) {
      newAv = false;
    }
    const obj = {
      available: newAv,
      startingTime: dateString,
    };

    //console.log("object" + JSON.stringify(obj));
    dispatch(changeAvailabilityHelper(obj));
  };

  return (
    <Wrapper>
      <header>
        <div className="main-icon">{socketId}</div>
        <div className="info">
          <h5>Id: {socketId}</h5>
          <h5>Type: {type}</h5>
        </div>
      </header>
      <div className="content">
        <div className="content-center">
          <ul>
            <JobInfoText text={`Availability: `} />
            <Switch onChange={handleAvailabilityUpdate} checked={isAvailable} />
            <JobInfoText
              text={`Status: ${status === "AVAILABLE" ? "Free" : status}`}
            />
          </ul>

          <div>
            <h2>
              {socket.chargingProfiles.length > 0 ? "ChargingProfiles" : ""}
            </h2>
            {socket.chargingProfiles &&
              socket.chargingProfiles.map((cp) => (
                <div key={cp.chargingProfileId}>
                  <h4>Id: {cp.chargingProfileId}</h4>

                  <p>recurrencyKind: {cp.recurrencyKind}</p>
                  <p>validFrom: {cp.validFrom}</p>
                  <p>validTo: {cp.validTo}</p>
                  <p>periods:</p>
                  <ul>
                    {cp.periods.map((period, index) => (
                      <li key={index}>
                        Start: {period.start}, Limit: {period.limit}
                      </li>
                    ))}
                  </ul>
                </div>
              ))}
          </div>
        </div>
        <footer></footer>
      </div>
    </Wrapper>
  );
};
export default Socket;
