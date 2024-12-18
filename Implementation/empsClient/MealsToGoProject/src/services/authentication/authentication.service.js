import customFetch from "../../utils/axios";

export const loginRequest = async (email, password) => {
  const obj = {
    username: email,
    password: password,
  };
  const url = "/login";

  const customInstance = customFetch();
  const resp = await customInstance.post(url, obj);
  const jwt = resp.data.jwt;
  const user = resp.data.user;

  return { user, jwt };
};

export const registerRequest = async (
  newEmail,
  mewUsername,
  newPassword,
  newName,
  newSurname
) => {
  const obj = {
    email: newEmail,
    username: mewUsername,
    password: newPassword,
    name: newName,
    surname: newSurname,
  };
  const url = "/register";
  const customInstance = customFetch();
  const resp = await customInstance.post(url, obj);

  return resp.data;
};
