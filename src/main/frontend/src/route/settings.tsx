import SettingInput from "../components/setting/setting-input";

const Settings = () => {
    return (
        <div>
            <h1 className={
                "text-4xl text-center text-foreground dark:text-foreground-dark font-bold"
            }>Settings</h1>
            <SettingInput settingKey={"test"} defaultValue={"dummy"} label={"Test"}/>
        </div>
    );
};

export default Settings;