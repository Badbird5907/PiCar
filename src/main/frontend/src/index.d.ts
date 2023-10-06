import ReconnectingWebSocket from "reconnecting-websocket";

declare global {
    interface Window {
        streamClosed: boolean;
        ws: ReconnectingWebSocket;
    }
}
export {}