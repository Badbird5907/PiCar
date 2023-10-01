import {Card, CardBody, Input} from "@nextui-org/react";
import {useEffect, useState} from "react";
import {findSetting, getSettingValue, Setting} from "@/util/setting.ts";
import CustomButton from "@/components/button.tsx";

type SettingInputProps = {
    settingKey: string,
    rerender?: number
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
    }, [props.rerender]);
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
                        <CustomButton className={"mt-4"} onClickLoading={() => {
                            // localforage.setItem(props.settingKey, value);
                            localStorage.setItem(props.settingKey, value);
                            return Promise.resolve();
                        }}>Save</CustomButton>
                    </>
                )}
            </CardBody>
        </Card>
    );
};

export default SettingInput;