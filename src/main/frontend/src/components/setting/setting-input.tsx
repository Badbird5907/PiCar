import {Button, Input} from "@nextui-org/react";
import {useEffect, useState} from "react";

type SettingInputProps = {
    settingKey: string;
    defaultValue: string;
    label: string;
}
const SettingInput = (props: SettingInputProps) => {
    const [value, setValue] = useState(props.defaultValue)
    useEffect(() => {
        const val = localStorage.getItem(props.settingKey)/*.then((val) => {
            if (val) {
                setValue(val as string);
            }
        });
        */
        if (val) {
            setValue(val);
        }
    }, []);
    return (
        <>
            <Input value={value} label={props.label} onChange={(e) => {
                const value = e.target.value;
                console.log(value)
                setValue(value);
            }}/>
            <Button onClick={() => {
                // localforage.setItem(props.settingKey, value);
                localStorage.setItem(props.settingKey, value);
            }}>Save</Button>
        </>
    );
};

export default SettingInput;