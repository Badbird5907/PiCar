import SettingInput from "../components/setting/setting-input";
import {Button} from "@nextui-org/react";
import {FaArrowLeft} from "react-icons/fa";

const Settings = () => {
    return (
        <div>
            <h1 className={
                "text-4xl text-center text-foreground dark:text-foreground-dark font-bold"
            }>Settings</h1>
            <a href={"/"}>
                <Button isIconOnly variant={"faded"} className={"left-0 top-0 absolute ml-2 mt-2"}>
                    <FaArrowLeft />
                </Button>
            </a>
            <div className={"grid grid-cols-1 md:grid-cols-8"}>
                <SettingInput settingKey={"apiUrl"} />
                <SettingInput settingKey={"streamWs"} />
            </div>
        </div>
    );
};

export default Settings;