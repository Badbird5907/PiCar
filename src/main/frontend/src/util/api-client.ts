import axios from "axios";
import {getSettingValue} from "@/util/setting";

const backend = getSettingValue("apiUrl");
axios.defaults.baseURL = backend;

export default axios;