import { useState, useEffect } from "react";
import {
  FormRow,
  FormRowSelect,
  FormRowSelectMultiple,
} from "../../components";
import { useLocation } from "react-router-dom";
import Wrapper from "../../assets/wrappers/DashboardFormPage";
import { useDispatch } from "react-redux";
import { toast } from "react-toastify";

import {
  setTariffSpecial,
  setTariffStandard,
} from "../../features/tariff/tariffSlice";

const initialState = {
  isLoading: false,
  tariff: {
    socketType: ["FAST"],
    startDate: "",
    endDate: "",
    price: 0.0,
    stepSize: 0, // in seconds
    isSpecialOffer: false,
    startTime: "00:00",
    endTime: "00:00",
    minKWh: 0.0,
    maxKWh: 0.0,
    minCurrent: 0.0,
    maxCurrent: 0.0,
    minDuration: 0, // in seconds
    maxDuration: 0, // in seconds
    daysOfTheWeek: [],
  },
};

const OptimizerManualSettingsEnergyTariff = (props) => {
  const location = useLocation();
  const cp = location.state;

  const [values, setValues] = useState(initialState);

  const dispatch = useDispatch();

  // Handle input change
  const handleInputChange = (e) => {
    const name = e.target.name;
    const value = e.target.value;
    setValues({ ...values, tariff: { ...values.tariff, [name]: value } });
  };

  // Handle multiple select
  const handleMultiple = (e) => {
    const name = e.target.name;
    const selectedValue = e.target.value;

    setValues({
      ...values,
      tariff: {
        ...values.tariff,
        daysOfTheWeek: [...values.tariff.daysOfTheWeek, selectedValue],
      },
    });
  };

  // Handle isSpecialOffer select
  const handleSelectSpecialOffer = (e) => {
    setValues({
      ...values,
      tariff: {
        ...values.tariff,
        isSpecialOffer: !values.tariff.isSpecialOffer,
      },
    });
  };

  // Handle clear
  const handleClear = (e) => {
    e.preventDefault();
    setValues(initialState);
  };

  // Handle submit
  const handleSubmit = (e) => {
    e.preventDefault();
    const {
      socketType,
      startDate,
      endDate,
      price,
      stepSize,
      isSpecialOffer,
      startTime,
      endTime,
      minKWh,
      maxKWh,
      minCurrent,
      maxCurrent,
      minDuration,
      maxDuration,
      daysOfTheWeek,
    } = values.tariff;

    // Validate required fields

    if (
      !socketType ||
      !startDate ||
      !endDate ||
      price <= 0.0 ||
      stepSize <= 0.0
    ) {
      toast.error("Please fill out all required fields");
      return;
    }

    if (isSpecialOffer) {
      // Validate special offer fields
      if (
        startTime === endTime ||
        minKWh <= 0.0 ||
        maxKWh <= 0.0 ||
        minDuration >= maxDuration ||
        minDuration <= 0.0 ||
        minCurrent > maxCurrent
      ) {
        toast.error("Please fill out all required fields for special offer");
        return;
      }
    }

    // Dispatch the appropriate action based on whether this is a standard or special offer
    if (!isSpecialOffer) {
      dispatch(
        setTariffStandard({
          tariff: {
            socketType,
            startDate,
            endDate,
            price,
            stepSize,
            isSpecialOffer,
            startTime: startTime || "00:00",
            endTime: endTime || "00:00",
            minKWh: minKWh || 0.0,
            maxKWh: maxKWh || 0.0,
            minCurrent: minCurrent || 0.0,
            maxCurrent: maxCurrent || 0.0,
            minDuration: minDuration || 0,
            maxDuration: maxDuration || 0,
            daysOfTheWeek: daysOfTheWeek || [],
          },
          cpId: cp.id,
        })
      );
    } else {
      dispatch(
        setTariffSpecial({
          tariff: {
            socketType,
            startDate,
            endDate,
            price,
            stepSize,
            isSpecialOffer,
            startTime,
            endTime,
            minKWh,
            maxKWh,
            minCurrent,
            maxCurrent,
            minDuration,
            maxDuration,
            daysOfTheWeek,
          },
          cpId: cp.id,
        })
      );
    }
  };

  return (
    <Wrapper>
      <form className="form">
        <h3>Add Energy Tariff</h3>
        <div className="form-center">
          {/* socketType */}
          <FormRowSelect
            name="socketType"
            value={values.tariff.socketType}
            handleChange={handleInputChange}
            list={["FAST", "RAPID", "SLOW"]}
            isMulti={true}
          />
          {/* startDate */}
          <FormRow
            type="date"
            name="startDate"
            value={values.tariff.startDate}
            handleChange={handleInputChange}
          />
          {/* endDate */}
          <FormRow
            type="date"
            name="endDate"
            value={values.tariff.endDate}
            handleChange={handleInputChange}
          />
          {/* price */}
          <FormRow
            type="number"
            name="price"
            value={values.tariff.price}
            handleChange={handleInputChange}
          />
          {/* stepSize */}
          <FormRow
            type="number"
            name="stepSize"
            value={values.tariff.stepSize}
            handleChange={handleInputChange}
          />
          {/* isSpecialOffer */}
          <FormRow
            type="checkbox"
            name="isSpecialOffer"
            handleChange={handleSelectSpecialOffer}
          />
          {values.tariff.isSpecialOffer && (
            <>
              {/* startTime */}
              <FormRow
                type="time"
                name="startTime"
                value={values.tariff.startTime}
                handleChange={handleInputChange}
              />
              {/* endTime */}
              <FormRow
                type="time"
                name="endTime"
                value={values.tariff.endTime}
                handleChange={handleInputChange}
              />
              {/* minKWh */}
              <FormRow
                type="number"
                name="minKWh"
                value={values.tariff.minKWh}
                handleChange={handleInputChange}
              />
              {/* maxKWh */}
              <FormRow
                type="number"
                name="maxKWh"
                value={values.tariff.maxKWh}
                handleChange={handleInputChange}
              />
              {/* minCurrent */}
              <FormRow
                type="number"
                name="minCurrent"
                value={values.tariff.minCurrent}
                handleChange={handleInputChange}
              />
              {/* maxCurrent */}
              <FormRow
                type="number"
                name="maxCurrent"
                value={values.tariff.maxCurrent}
                handleChange={handleInputChange}
              />
              {/* minDuration */}
              <FormRow
                type="number"
                name="minDuration"
                value={values.tariff.minDuration}
                handleChange={handleInputChange}
              />
              <FormRow
                type="number"
                name="maxDuration"
                value={values.tariff.maxDuration}
                handleChange={handleInputChange}
              />
              <FormRowSelectMultiple
                name="daysOfTheWeek"
                value={values.tariff.daysOfTheWeek}
                handleChange={handleMultiple}
                list={[
                  "Monday",
                  "Tuesday",
                  "Wednesday",
                  "Thursday",
                  "Friday",
                  "Saturday",
                  "Sunday",
                ]}
              />
            </>
          )}

          <div className="btn-container">
            <button
              type="button"
              className="btn btn-block clear-btn"
              onClick={handleClear}
            >
              clear
            </button>
            <button
              type="submit"
              className="btn btn-block submit-btn"
              onClick={handleSubmit}
            >
              submit
            </button>
          </div>
        </div>
      </form>
    </Wrapper>
  );
};

export default OptimizerManualSettingsEnergyTariff;
