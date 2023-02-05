import { useState } from "react";
import { FormRow } from "../../components";
import Wrapper from "../../assets/wrappers/DashboardFormPage";
import { useSelector, useDispatch } from "react-redux";
import { toast } from "react-toastify";
import { FormRowSelect2, FormRowSelect } from "../../components/FormRowSelect";
import { addCpT } from "../../features/cp/cpSlice";

function AddCp() {
  const initialState = {
    // general info for the CP
    cpId: "",
    address: "",
    latitude: "",
    name: "",
    longitude: "",
    authenticationKey: "",
    connectionUrl: "",

    // info of the socket form
    socketId: "",
    socketType: "FAST",

    // info battery part
    batteryId: "",
    maxCapacity: 0.0,
    minLevel: 0.0,
    maxLevel: 0.0,
    percent: 0.0,

    // tariff part
    tariffId: "",
    socketTypeTariff: "FAST",
    startDate: "",
    endDate: "",
    price: 0.0,
    stepSize: 0.0,
    isSpecialOffer: false,

    // what to return
    sockets: [],
    batteries: [],
    tariffs: [],
  };

  const dispatch = useDispatch();
  const [values, setValues] = useState(initialState);

  const handleInput = (e) => {
    e.preventDefault();
    const name = e.target.name;
    const value = e.target.value;
    setValues({ ...values, [name]: value });
    console.log("state=>" + JSON.stringify(values));
  };

  const handleMultiple = (e) => {
    e.preventDefault();
    const name = e.target.name;
    const value = e.target.value;
    setValues({ ...values, [name]: JSON.stringify(values) });
  };

  const handleSubmitAddBattery = (e) => {
    e.preventDefault();
    const { batteryId, maxCapacity, minLevel, maxLevel, percent } = values;

    if (!batteryId || !maxCapacity || maxCapacity < 0 || minLevel > maxLevel) {
      toast.error("Error! Invalid or Missing input");
      return;
    } else {
      const batteryNew = {
        batteryId: batteryId,
        maxCapacity: maxCapacity,
        minLevel: minLevel,
        maxLevel: maxLevel,
        percent: percent,
      };
      setValues({ ...values, batteries: [...values.batteries, batteryNew] });
      toast.success("Battery Added ");
    }
  };

  const handleSubmitAddTariff = (e) => {
    e.preventDefault();
    const {
      tariffId,
      socketTypeTariff,
      startDate,
      endDate,
      price,
      stepSize,
      isSpecialOffer,
    } = values;

    if (
      !socketTypeTariff ||
      !startDate ||
      !endDate ||
      !price ||
      !stepSize ||
      endDate < startDate ||
      price < 0
    ) {
      toast.error("ivalid or missing input, retry");
    } else {
      const tariffNew = {
        tariffId: tariffId,
        socketType: socketTypeTariff,
        startDate: startDate,
        endDate: endDate,
        price: price,
        stepSize: stepSize,
        isSpecialOffer: isSpecialOffer,
      };
      setValues({ ...values, tariffs: [...values.tariffs, tariffNew] });
      toast.success("Tariff Added ");
    }
  };

  const handleSubmitAddSocket = (e) => {
    e.preventDefault();
    const { socketId, socketType } = values;

    if (!socketId) {
      toast.error("provide a socket id");
      return;
    }
    const socketNew = {
      socketId: socketId,
      socketType: socketType,
    };
    setValues({ ...values, sockets: [...values.sockets, socketNew] });
    toast.success("Socket Added");
  };

  const handleSubmitSaveChanges = (e) => {
    e.preventDefault();
    const {
      cpId,
      address,
      latitude,
      longitude,
      authenticationKey,
      connectionUrl,
      sockets,
      tariffs,
      batteries,
      name,
    } = values;
    if (
      !cpId ||
      !address ||
      !latitude ||
      !longitude ||
      !authenticationKey ||
      !connectionUrl
    ) {
      toast.error("please fill out all CP s information");
      return;
    }
    const obj = {
      cpId: cpId,
      name: name,
      address: address,
      latitude: latitude,
      longitude: longitude,
      tariffs: tariffs,
      sockets: sockets,
      batteries: batteries,
      authenticationKey: authenticationKey,
      connectionUrl: connectionUrl,
    };
    dispatch(addCpT(obj));
    // also renitiatye the slice
    setValues(initialState);
  };

  const handleSelectSpecialOffer = (e) => {
    setValues({
      ...values,
      specialOffer: !values.specialOffer,
    });
  };

  return (
    <Wrapper>
      <form className="form">
        <h3>{"Insert Cp Inforation"}</h3>
        <div className="form-center">
          <FormRow
            type="text"
            name="cpId"
            value={values.cpId}
            handleChange={handleInput}
          />
          <FormRow
            type="text"
            name="name"
            value={values.name}
            handleChange={handleInput}
          />

          <FormRow
            type="text"
            name="address"
            value={values.address}
            handleChange={handleInput}
          />

          <FormRow
            type="text"
            name="latitude"
            value={values.latitude}
            handleChange={handleInput}
          />

          <FormRow
            type="text"
            name="longitude"
            value={values.longitude}
            handleChange={handleInput}
          />

          <FormRow
            type="text"
            name="connectionUrl"
            value={values.connectionUrl}
            handleChange={handleInput}
          />

          <FormRow
            type="text"
            name="authenticationKey"
            value={values.authenticationKey}
            handleChange={handleInput}
          />
        </div>
      </form>

      <form className="form">
        <h3>{"Add Socket to  Cp"}</h3>
        <div className="form-center">
          <FormRowSelect
            name="socketType"
            value={values.socketType}
            handleChange={handleInput}
            list={["FAST", "RAPID", "SLOW"]}
            mutilpe={true}
          />

          <FormRow
            type="number"
            name="socketId"
            value={values.socketId}
            handleChange={handleInput}
          />
          <div className="btn-container">
            <button
              type="submit"
              className="btn btn-block submit-btn"
              onClick={handleSubmitAddSocket}
            >
              Add Socket
            </button>
          </div>
        </div>
      </form>

      <form className="form">
        <h3>{"Add Battery to  Cp"}</h3>
        <div className="form-center">
          <FormRow
            type="text"
            name="batteryId"
            value={values.batteryId}
            handleChange={handleInput}
          />

          <FormRow
            type="number"
            name="maxCapacity"
            value={values.maxCapacity}
            handleChange={handleInput}
          />

          <FormRow
            type="number"
            name="minLevel"
            value={values.minLevel}
            handleChange={handleInput}
          />

          <FormRow
            type="number"
            name="maxLevel"
            value={values.maxLevel}
            handleChange={handleInput}
          />

          <FormRow
            type="number"
            name="percent"
            value={values.percent}
            handleChange={handleInput}
          />

          <div className="btn-container">
            <button
              type="submit"
              className="btn btn-block submit-btn"
              onClick={handleSubmitAddBattery}
            >
              Add Battery
            </button>
          </div>
        </div>
      </form>

      <form className="form">
        <h3>{"Add Tariff to  Cp"}</h3>
        <div className="form-center">
          <FormRowSelect
            name="socketTypeTariff"
            value={values.socketTypeTariff}
            handleChange={handleInput}
            list={["FAST", "RAPID", "SLOW"]}
            isMulti={true}
          />

          <FormRow
            type="date"
            name="startDate"
            value={values.startDate}
            handleChange={handleInput}
          />

          <FormRow
            type="date"
            name="endDate"
            value={values.endDate}
            handleChange={handleInput}
          />

          <FormRow
            type="number"
            name="price"
            value={values.price}
            handleChange={handleInput}
          />

          <FormRow
            type="number"
            name="stepSize"
            value={values.stepSize}
            handleChange={handleInput}
          />
          <FormRow
            type="checkbox"
            name="isSpecialOffer"
            handleChange={handleSelectSpecialOffer}
          />

          <div className="btn-container">
            <button
              type="submit"
              className="btn btn-block submit-btn"
              onClick={handleSubmitAddTariff}
            >
              Add Tariff
            </button>
          </div>
        </div>
        <footer>
          <h3>Resume:</h3>
          <ul>
            <li>Number of Sockets Added : {values.sockets.length}</li>
            <li> Number of Batteries added: {values.batteries.length}</li>
            <li> Number of TariffsAdded : {values.tariffs.length}</li>
          </ul>

          <div className="btn-container">
            <button
              type="submit"
              className="btn btn-block submit-btn"
              onClick={handleSubmitSaveChanges}
            >
              Confirm
            </button>
          </div>
        </footer>
      </form>
    </Wrapper>
  );
}
export default AddCp;
