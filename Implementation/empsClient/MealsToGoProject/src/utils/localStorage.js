/*import AsyncStorage from "@react-native-async-storage/async-storage";

export const addUserToLocalStorage = async (user) => {
  try {
    await AsyncStorage.setItem("user", JSON.stringify(user));
  } catch (error) {
    console.log("error");
    console.log("error");
    console.log("error");
    console.log("error");
    console.log("error");
  }
};

export const addTokenToLocalStorage = async (token) => {
  try {
    await AsyncStorage.setItem("token", JSON.stringify(token));
  } catch (error) {
    console.log("error");
    console.log("error");
    console.log("error");
    console.log("error");
    console.log("error");
  }
};

export const getTokenFromLocalStorage = async () => {
  try {
    const token = await AsyncStorage.getItem("token");
    return token;
  } catch (error) {
    console.log("error");
    console.log("error");
    console.log("error");
    console.log("error");
    console.log("error");
  }
};

export const getUserFromLocalStorage = async () => {
  try {
    const user = await AsyncStorage.getItem("user");
    if (user === null || user === undefined) {
      console.log("thereeee");
      console.log("thereeee");
      console.log("thereeee");
      console.log("thereeee");
      console.log("thereeee");
      return user;
    }
    console.log("sendin user" + user);
    return user;
  } catch (error) {
    console.log("error user");
    console.log("error");
    console.log("error");
    console.log("error");
    console.log("error");
  }
};

export const removeUserFromLocalStorage = async () => {
  try {
    await AsyncStorage.removeItem("user");
  } catch (error) {
    console.log("error rem" + JSON.stringify(error));
    console.log("error");
    console.log("error");
    console.log("error");
    console.log("error");
    console.log("error");
  }
};

export const removeTokenFromLocalStorage = async () => {
  try {
    await AsyncStorage.removeItem("token");
  } catch (error) {
    console.log("error");
    console.log("error");
    console.log("error");
    console.log("error");
  }
};
*/
