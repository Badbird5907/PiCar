import {Button, Card, CardBody, Input} from "@nextui-org/react";
import {useEffect, useState} from "react";
import {findSetting, getSettingValue, Setting} from "@/util/setting.ts";

type SettingInputProps = {
    settingKey: string,
}
const SettingInput = (props: SettingInputProps) => {
    const [setting, setSetting] = useState<Setting | undefined>(undefined)
    const [value, setValue] = useState<string>("");
    useEffect(() => {
        const foundSetting = findSetting(props.settingKey);
        setSetting(foundSetting);
        if (foundSetting) {
            setValue(getSettingValue(foundSetting.key))
        }
    }, []);
    return (
        <Card className={"col-span-2 m-6 md:mr-3 sm:mb-3 h-fit"}>
            <CardBody>
                {setting && (
                    <>
                        <Input value={value} label={setting.label} onChange={(e) => {
                            const value = e.target.value;
                            console.log(value)
                            setValue(value);
                        }}/>
                        <Button className={"mt-4"} onClick={() => {
                            // localforage.setItem(props.settingKey, value);
                            localStorage.setItem(props.settingKey, value);
                        }}>Save</Button>
                    </>
                )}
            </CardBody>
        </Card>
    );
};

export default SettingInput;