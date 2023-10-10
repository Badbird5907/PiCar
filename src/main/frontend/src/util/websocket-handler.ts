import ReconnectingWebSocket from "reconnecting-websocket";
import {info} from "@/util/log";
import {getSettingValue} from "@/util/setting.ts";

const init = () => {
    info("Initializing main WebSocket connection");
    const wsUrl = getSettingValue("websocketUrl");
    const ws = window.ws = new ReconnectingWebSocket(wsUrl);
    ws.addEventListener("open", () => {
        info("Main WebSocket connection opened");
    })
    ws.addEventListener("close", () => {
        info("Main WebSocket connection closed");
    });
    return ws;
}
const close = () => {
    window.ws.close();
}

export default {
    init,
    close,
}
