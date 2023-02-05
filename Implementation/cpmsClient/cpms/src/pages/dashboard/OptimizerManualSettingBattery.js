import { useState } from "react";
import Wrapper from "../../assets/wrappers/DashboardFormPage";
import { toast } from "react-toastify";
import { useLocation } from "react-router-dom";
import { FormRow } from "../../components";
import { settingBattery } from "../../features/battery/batteryThunk";

const OptimizerManualSettingsBattery = () => {
  const location = useLocation();

  const battery = location.state.battery;
  const cp = location.state.cp;
  console.log("battery obtained" + JSON.stringify(battery));
  console.log("cp obtained" + JSON.stringify(cp));
  const [values, setValues] = useState({ battery });
  console.log("values obtained" + JSON.stringify(values));
  const handleInputChange = (e) => {
    const name = e.target.name;
    let value = e.target.value;
    if (name === "minLevel" || name === "maxLevel") {
      value = parseInt(value, 10);
    }
    setValues({ ...values, battery: { ...values.battery, [name]: value } });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const { minLevel, maxLevel, percent } = values.battery;
    console.log("minLivel" + minLevel);
    if (minLevel > maxLevel || minLevel < 0) {
      return toast.error("Minimum/ Maximum Level Invalid");
    }
    if (percent > 100.0 || percent < 0.0) {
      return toast.error("Percent of usage invalid");
    } else {
      const batterySetting = { ...values.battery };
      delete batterySetting["status"];
      delete batterySetting["maxCapacity"];
      delete batterySetting["batteryId"];
      console.log("battery settings" + JSON.stringify(batterySetting));

      if (!values.battery) {
        const url = "";
      }

      const url = `chargingPoints/${cp.id}/energySources/battery/${values.battery.batteryId}`;
      try {
        await settingBattery(batterySetting, url);
        toast.success("Success");
      } catch (error) {
        toast.error("Invalid request");
      }
    }
  };

  if (!values.battery) {
    return <h1>This CP has no Battery</h1>;
  }

  return (
    <Wrapper>
      <form className="form">
        <h3> Battry Info</h3>
        <div className="form-center">
          <FormRow
            type="number"
            name="minLevel"
            value={values.battery.minLevel}
            handleChange={handleInputChange}
          />
          <FormRow
            type="number"
            name="maxLevel"
            value={values.battery.maxLevel}
            handleChange={handleInputChange}
          />
          <FormRow
            type="number"
            name="percent"
            value={values.battery.percent}
            handleChange={handleInputChange}
          />
        </div>

        <footer>
          <div className="btn-container">
            <button
              type="submit"
              className="btn btn-block submit-btn"
              onClick={handleSubmit}
            >
              Submit
            </button>
          </div>
        </footer>
      </form>
    </Wrapper>
  );
};

export default OptimizerManualSettingsBattery;
