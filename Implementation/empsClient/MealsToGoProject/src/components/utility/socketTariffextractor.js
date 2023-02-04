import React, { useState, useEffect } from "react";

export const socketTariff = (cp) => {
  console.log("here modified soxket before" + JSON.stringify(cp.sockets));
  let modifiedSockets = cp.sockets
    .filter((socket) => socket.availability === "AVAILABLE")
    .map((socket) => {
      for (let i = 0; i < cp.tariffs.length; i++) {
        if (socket.type === cp.tariffs[i].socketType) {
          socket = { ...socket, ...cp.tariffs[i] };
          break;
        }
      }
      Object.keys(socket).forEach((key) => {
        if (typeof socket[key] === "string") {
          socket[key] = socket[key].toUpperCase();
        }
      });
      return socket;
    });
  console.log("here modified soxket after" + JSON.stringify(modifiedSockets));
  return modifiedSockets;
};
