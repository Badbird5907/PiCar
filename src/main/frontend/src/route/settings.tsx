import SettingInput from "../components/setting/setting-input";
import {FaArrowLeft} from "react-icons/fa";
import CustomButton from "@/components/button.tsx";
import {Card, CardBody} from "@nextui-org/react";
import {useState} from "react";

const Settings = () => {
    const [dummy, setDummy] = useState(0); // dummy state to force rerender
    return (
        <div>
            <h1 className={
                "text-4xl text-center text-foreground dark:text-foreground-dark font-bold"
            }>Settings</h1>
            <a href={"/"}>
                <CustomButton isIconOnly variant={"faded"} className={"left-0 top-0 absolute ml-2 mt-2"}>
                    <FaArrowLeft/>
                </CustomButton>
            </a>
            <div className={"grid grid-cols-1 md:grid-cols-8"}>
                <Card className={"col-span-2 m-6 md:mr-3 sm:mb-3 h-fit"}>
                    <CardBody>
                        <CustomButton onClickLoading={async () => {
                            localStorage.setItem("apiUrl", "http://localhost:1337/");
                            localStorage.setItem("streamWs", "ws://localhost:1337/api/ws/stream");
                            setDummy(dummy + 1)
                        }}>Use Localhost</CustomButton>
                        <CustomButton className={"mt-4"} onClickLoading={async () => {
                            localStorage.setItem("apiUrl", "http://192.168.3.69:1337/");
                            localStorage.setItem("streamWs", "ws://192.168.3.69:1337/api/ws/stream");
                            setDummy(dummy + 1)
                        }}>Use Pi</CustomButton>
                        <div className={"w-full mt-4"}>
                            <span>API: </span>
                            <CustomButton className={"ml-4 mr-2 w-fit"} onClickLoading={async () => {
                                localStorage.setItem("apiUrl", "http://192.168.3.69:1337/");
                                setDummy(dummy + 1)
                            }}>Pi</CustomButton>
                            <CustomButton className={"w-fit"} onClickLoading={async () => {
                                localStorage.setItem("apiUrl", "http://localhost:1337/");
                                setDummy(dummy + 1)
                            }}>localhost</CustomButton>
                        </div>
                        <div className={"w-full mt-4"}>
                            <span>Stream: </span>
                            <CustomButton className={"ml-4 mr-2 w-fit"} onClickLoading={async () => {
                                localStorage.setItem("streamWs", "ws://192.168.3.69:1337/api/ws/stream");
                                setDummy(dummy + 1)
                            }}>Pi</CustomButton>
                            <CustomButton className={"w-fit"} onClickLoading={async () => {
                                localStorage.setItem("streamWs", "ws://localhost:1337/api/ws/stream");
                                setDummy(dummy + 1)
                            }}>localhost</CustomButton>
                        </div>
                    </CardBody>
                </Card>
                <SettingInput rerender={dummy} settingKey={"apiUrl"}/>
                <SettingInput rerender={dummy} settingKey={"streamWs"}/>
            </div>
        </div>
    );
};

export default Settings;