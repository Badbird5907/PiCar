import {useEffect, useRef, useState} from "react";
import ReconnectingWebSocket from "reconnecting-websocket";

type MotorControlProps = {
    ws: ReconnectingWebSocket | null;
}
const MotorControl = (props: MotorControlProps) => {
    const [_down, _setDown] = useState<string>("");
    const downRef = useRef(_down);
    const setDown = (data: string) => {
        downRef.current = data;
        _setDown(data);
    }
    const shouldRecord = (e: KeyboardEvent) => {
        if (e.target instanceof HTMLInputElement) return false;
        if (e.target instanceof HTMLTextAreaElement) return false;
        return !e.repeat;
    }

    useEffect(() => {
        // we use one down variable to prevent multiple keys being pressed at once
        // this is because the backend can't handle multiple keys being pressed at once
        const kdListener = (e: KeyboardEvent) => {
            if (!shouldRecord(e)) return;
            if ("wasd".includes(e.key)) {
                e.preventDefault()
                setDown(e.key)
                // console.log("kd", e.key, downRef.current)
            }
        }
        const kuListener = (e: KeyboardEvent) => {
            if (!shouldRecord(e)) return;
            if ("wasd".includes(e.key)) {
                e.preventDefault()
                // console.log("Key: ",e.key,"Expected: ",downRef.current)
                if (e.key === downRef.current) {
                    // console.log("ku", e.key)
                    setDown("")
                }
            }
        }
        window.addEventListener("keydown", kdListener)
        window.addEventListener("keyup", kuListener)
        return () => {
            window.removeEventListener("keydown", kdListener)
            window.removeEventListener("keyup", kuListener)
        }
    }, []);

    useEffect(() => {
        if (!props.ws) return;
        const state = _down ? _down : "stop";
        // console.log("sending", state)
        props.ws.send(JSON.stringify({
            name: "motor",
            data: {
                state
            }
        }))
    }, [_down]);
    return null;
};

export default MotorControl;