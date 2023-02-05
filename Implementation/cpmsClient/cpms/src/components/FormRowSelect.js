export const FormRowSelect = ({
  labelText,
  name,
  value,
  handleChange,
  list,
}) => {
  return (
    <div className="form-row">
      <label htmlFor={name} className="form-label">
        {labelText || name}
      </label>
      <select
        name={name}
        id={name}
        value={value}
        onChange={handleChange}
        className="form-select"
      >
        {list.map((itemValue, index) => {
          return (
            <option key={index} value={itemValue}>
              {itemValue}
            </option>
          );
        })}
      </select>
    </div>
  );
};

export const FormRowSelect2 = ({
  labelText,
  name,
  value,
  handleChange,
  list,
  isMulti,
}) => {
  return (
    <div className="form-row">
      <label htmlFor={name} className="form-label">
        {labelText || name}
      </label>
      <select
        name={name}
        id={name}
        value={isMulti ? value : value[0]}
        onChange={handleChange}
        className="form-select"
        multiple={isMulti}
      >
        {list.map((itemValue, index) => {
          return (
            <option key={index} value={itemValue}>
              {itemValue}
            </option>
          );
        })}
      </select>
    </div>
  );
};
export default { FormRowSelect, FormRowSelect2 };
