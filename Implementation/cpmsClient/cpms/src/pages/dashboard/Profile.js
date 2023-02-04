import { useState } from "react";
import { FormRow } from "../../components";
import Wrapper from "../../assets/wrappers/DashboardFormPage";
import { useDispatch, useSelector } from "react-redux";
import { toast } from "react-toastify";
import { updateUser } from "../../features/user/userSlice";

const Profile = () => {
  const { isLoading, user } = useSelector((store) => store.user);

  const dispatch = useDispatch();

  const [password, setpassword] = useState({
    oldPassword: "",
    newPassword: "",
  });

  console.log(user.cpoCode);
  const cpoCode = user.cpoCode;
  const iban = user.iban;
  const username = user.username;

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!password.oldPassword || !password.newPassword) {
      toast.error("please insert valid input ");
      return;
    }
    dispatch(updateUser(password));
  };

  const handleChange = (e) => {
    console.log(e.target.name + "  target name ");
    console.log(e.target.value + "  target value");
    const name = e.target.name;
    const value = e.target.value;
    setpassword({ ...password, [name]: value });
  };

  return (
    <Wrapper>
      <form className="form" onSubmit={handleSubmit}>
        <h3>profile</h3>
        <div className="form-center">
          <ul>
            <li>Cpo-Code: {cpoCode}</li>
            <li>Iban: {iban}</li>
            <li>Username: {username}</li>
          </ul>
          <FormRow
            type="password"
            name="newPassword"
            value={password.newPassword}
            handleChange={handleChange}
          />
          <FormRow
            type="password"
            name="oldPassword"
            value={password.oldPassword}
            handleChange={handleChange}
          />
          <button type="submit" className="btn btn-block" disabled={isLoading}>
            {isLoading ? "Please Wait..." : "save changes"}
          </button>
        </div>
      </form>
    </Wrapper>
  );
};
export default Profile;

/*

  return (
    <div>
      <h1>Bella signo </h1>
    </div>
  );
*/
