export type Setting = {
    label: string;
    key: string;
    defaultValue: any;
}
export const settings: Setting[] = [
    {
        label: "API URL",
        key: "apiUrl",
        defaultValue: "http://192.168.3.69:1337/",
    },
    {
        label: "Stream WS",
        key: "streamWs",
        defaultValue: "ws://192.168.3.69:1337/api/ws/stream",
    },
    {
        label: "Websocket URL",
        key: "websocketUrl",
        defaultValue: "ws://192.168.3.69:1337/ws",
    }
]

export function findSetting(key: string) {
    return settings.find(setting => setting.key === key);
}
export function getSettingValue(key: string) {
    const setting = findSetting(key);
    if (!setting) {
        throw new Error(`Setting ${key} not found`);
    }
    const value = localStorage.getItem(key);
    if (value) {
        return value;
    }
    return setting.defaultValue;
}