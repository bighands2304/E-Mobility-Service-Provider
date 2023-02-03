import React from "react";

const ChargingProfiles = ({ cpData }) => {
  return (
    <div>
      {cpData.map((cp) => (
        <div key={cp.Id}>
          <p>Id: {cp.Id}</p>
          <p>RecurrencyKind: {cp.RecurrencyKind}</p>
          <p>ValidFrom: {cp.ValidFrom}</p>
          <p>ValidTo: {cp.ValidTo}</p>
          <p>Periods:</p>
          <ul>
            {cp.Periods.map((period, index) => (
              <li key={index}>
                Start: {period.Start}, Limit: {period.Limit}
              </li>
            ))}
          </ul>
        </div>
      ))}
    </div>
  );
};

export default ChargingProfiles;
