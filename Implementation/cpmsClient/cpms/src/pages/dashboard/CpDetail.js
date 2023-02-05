import React, { useState } from "react";
import { FaLocationArrow } from "react-icons/fa";
import { Link, useLocation } from "react-router-dom";
import Wrapper from "../../assets/wrappers/Job";
import { CPInfo } from "../../components/CPInfo";
import Switch from "react-switch";
import { useDispatch } from "react-redux";
import Socket from "../../components/Socket";
import { toast } from "react-toastify";
import Tariff from "./Tariff";
import { createAsyncThunk } from "@reduxjs/toolkit";
import Battery from "./Battery";
import ToogleOptimizerThunk from "../../features/optimizer/TaggleOptimizerThunk";

const CpDetail = (props) => {
  const location = useLocation();
  const cp = location.state;
  const dispatch = useDispatch();

  const [togglePriceOptimizer, setTogglePriceOptimizer] = useState(
    cp.togglePriceOptimizer
  );
  const [toggleEnergyMixOptimizer, setToggleEnergyMixOptimizer] = useState(
    cp.toggleEnergyMixOptimizer
  );
  const [toggleDSOSelectionOptimizer, setToggleDSOSelectionOptimizer] =
    useState(cp.toggleDSOSelectionOptimizer);

  const toggleOptimizerEnergy = createAsyncThunk(
    `/energytoggle/}`,
    async (thunkAPI) => {
      const url = `/chargingPoints/${
        cp.id
      }/optimizer/energyMix?automaticMode=${!toggleEnergyMixOptimizer}`;

      ToogleOptimizerThunk(url, thunkAPI).then((resp) => {
        console.log("resp" + JSON.stringify(resp.status));
        if (resp.status === 200) {
          toast.success("Success");
          setToggleEnergyMixOptimizer(!toggleEnergyMixOptimizer);
        } else {
          toast.error("An error occurred");
          return false;
        }
      });
    }
  );

  const toggleOptimizerDSO = createAsyncThunk(
    `/energytoggle/}`,
    async (thunkAPI) => {
      const url = `/chargingPoints/${
        cp.id
      }/optimizer/dsoSelection?automaticMode=${!toggleDSOSelectionOptimizer}`;

      ToogleOptimizerThunk(url, thunkAPI).then((resp) => {
        console.log("resp" + JSON.stringify(resp.status));
        if (resp.status === 200) {
          toast.success("Success");
          setToggleDSOSelectionOptimizer(!toggleDSOSelectionOptimizer);
        } else {
          toast.error("An error occurred");
          return false;
        }
      });
    }
  );

  const toggleOptimizerPrice = createAsyncThunk(
    `/pricetoggle/}`,
    async (thunkAPI) => {
      const url = `/chargingPoints/${
        cp.id
      }/optimizer/price?automaticMode=${!toggleOptimizerPrice}`;

      ToogleOptimizerThunk(url, thunkAPI).then((resp) => {
        if (resp.status === 200) {
          toast.success("Success");
          setTogglePriceOptimizer(!togglePriceOptimizer);
        } else {
          toast.error("An error occurred");
          return false;
        }
      });
    }
  );

  const ToggleTrigger = (type) => {
    if (type === "energy") {
      dispatch(toggleOptimizerEnergy());
    }
    if (type === "dso") {
      dispatch(toggleOptimizerDSO());
    }
    if (type === "tariff") {
      dispatch(toggleOptimizerPrice());
    }
  };

  return (
    <Wrapper>
      <header>
        <div className="main-icon"></div>
        <div className="info">
          <h5>Cp id : {cp.cpId}</h5>
          <h5>Cp Name : {cp.name}</h5>
        </div>
      </header>
      <div className="content">
        <div className="content-center">
          <CPInfo icon={<FaLocationArrow />} text={cp.address} />
          <ul>
            <li>
              Price Optimizer{" "}
              <Switch
                checked={togglePriceOptimizer}
                onChange={() => {
                  ToggleTrigger("tariff");
                }}
              />
              {!togglePriceOptimizer && (
                <Link to="/opt-tariff" state={cp}>
                  Price Optimizer
                </Link>
              )}
            </li>
            <li>
              Energy Mix Optimizer{" "}
              <Switch
                checked={toggleEnergyMixOptimizer}
                onChange={() => {
                  ToggleTrigger("energy");
                }}
              />
              {!toggleEnergyMixOptimizer && (
                <Link to="/opt-battery">Energy Mix Optimizer</Link>
              )}
            </li>
            <li>
              DSO Selection Optimizer
              <Switch
                checked={toggleDSOSelectionOptimizer}
                onChange={() => {
                  ToggleTrigger("dso");
                }}
              />
              {!toggleDSOSelectionOptimizer && (
                <Link to="/opt-aquisition">DSO Selection Optimizer</Link>
              )}
            </li>
          </ul>
          <Link to="/all-offers" state={cp}>
            View ALL Offers
          </Link>
        </div>
        <h1>Sockets : </h1>
        <div className="sockets">
          {cp.sockets.map((socket) => {
            return (
              <Socket key={socket.id} {...socket} socket={socket} cp={cp} />
            );
          })}
        </div>
        <h1>Tariffs : </h1>
        <div className="tariffs">
          {cp.tariffs.map((tariff) => {
            return <Tariff key={tariff.tariffId} {...tariff} tariff={tariff} />;
          })}
        </div>
        <h1>Batteries : </h1>
        <div className="batteries">
          {cp.batteries.map((battery) => {
            return (
              <Battery
                key={battery.batteryId}
                {...battery}
                battery={battery}
                cp={cp}
              />
            );
          })}
        </div>

        <footer>
          <Link to="/all-cps" state={cp}>
            {" "}
            Back
          </Link>
        </footer>
      </div>
    </Wrapper>
  );
};
export default CpDetail;
