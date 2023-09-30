import SettingInput from "../components/setting/setting-input";

const Settings = () => {
    return (
        <div>
            <h1 className={
                "text-4xl text-center text-foreground dark:text-foreground-dark font-bold"
            }>Settings</h1>
            <div className={"grid grid-cols-1 md:grid-cols-8"}>
                <SettingInput settingKey={"apiUrl"} />
                <SettingInput settingKey={"streamWs"} />
            </div>
        </div>
    );
};

export default Settings;