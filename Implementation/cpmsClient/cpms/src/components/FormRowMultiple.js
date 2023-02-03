import React, { useState } from "react";

const FormRowSelectMultiple = ({
  labelText,
  name,
  value,
  handleChange,
  list,
}) => {
  const [selectedValues, setSelectedValues] = useState(value || []);

  const handleOptionChange = (event) => {
    const selectedOption = event.target.value;
    let newSelectedValues;
    if (selectedValues.includes(selectedOption)) {
      newSelectedValues = selectedValues.filter(
        (value) => value !== selectedOption
      );
    } else {
      newSelectedValues = [...selectedValues, selectedOption];
    }
    setSelectedValues(newSelectedValues);
    handleChange(event);
  };

  return (
    <div className="form-row">
      <label htmlFor={name} className="form-label">
        {labelText || name}
      </label>
      <select
        name={name}
        id={name}
        value={selectedValues}
        onChange={handleOptionChange}
        className="form-select"
        multiple
        style={{ height: "100px" }}
      >
        {list.map((itemValue, index) => {
          return (
            <option key={index} value={itemValue}>
              {selectedValues.includes(itemValue) ? "✔️" : ""} {itemValue}
            </option>
          );
        })}
      </select>
    </div>
  );
};
export default FormRowSelectMultiple;
