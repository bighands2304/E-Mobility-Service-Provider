import { IoBarChartSharp } from "react-icons/io5";
import { MdQueryStats } from "react-icons/md";
import { FaWpforms } from "react-icons/fa";
import { ImProfile } from "react-icons/im";

const links = [
  { id: 1, text: "stats", path: "/", icon: <IoBarChartSharp /> },
  { id: 2, text: "all cps", path: "all-cps", icon: <MdQueryStats /> },
  { id: 3, text: "add cp", path: "add-cp", icon: <FaWpforms /> },
  { id: 4, text: "offers", path: "offers", icon: <ImProfile /> },
  { id: 5, text: "profile", path: "profile", icon: <ImProfile /> },
];

export default links;
