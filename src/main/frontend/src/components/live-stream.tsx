import {useEffect, useRef, useState} from "react";
import {getSettingValue} from "@/util/setting.ts";
import {Card, CardBody, Spinner} from "@nextui-org/react";
import {FaPause, FaPlay} from "react-icons/fa";
import {FaArrowsRotate} from "react-icons/fa6";
import CustomButton from "@/components/button.tsx";
import {error, info} from "@/util/log";

type LiveStreamProps = {
    refresh: () => void
    close: () => void
}
const LiveStream = (props: LiveStreamProps) => {
    const [loading, setLoading] = useState(true);
    const [fps, setFps] = useState(0);
    const [ws, setWs] = useState<WebSocket | undefined>(undefined);
    const lastFrameTime = useRef(0);
    const [stopped, setStopped] = useState(false);

    const setupWebSocket = () => {
        if (stopped || window.streamClosed) {
            return;
        }
        const video = document.getElementById("video") as HTMLVideoElement;

        const webSocket = new WebSocket(getSettingValue("streamWs"));
        setWs(webSocket);
        info("[VIDEO] Connecting to WebSocket...");
        webSocket.addEventListener("open", (_) => {
            info("[VIDEO] WebSocket connection opened.");
            setLoading(false);
            lastFrameTime.current = performance.now();
        });
        webSocket.addEventListener("message", (event) => {
            if (!video || window.streamClosed) return;
            const base64Image = event.data;
            video.src = `data:image/jpeg;base64,${base64Image}`;

            // Calculate FPS
            const now = performance.now();
            const elapsed = now - lastFrameTime.current;
            lastFrameTime.current = now;
            const newFps = Math.round(1000 / elapsed); // Frames per second
            setFps(newFps);

            if (loading) {
                setLoading(false);
            }
        });
        webSocket.addEventListener("error", (event) => {
            error("[VIDEO] WebSocket error:", event);
        });
        webSocket.addEventListener("close", (_) => {
            info("[VIDEO] WebSocket connection closed.");
            if (stopped || !video || window.streamClosed) {
                return;
            }
            setLoading(true);
            setTimeout(function () {
                info("[VIDEO] Reconnecting...")
                setupWebSocket();
            }, 1000);
        });
        return webSocket;
    }

    useEffect(() => {
        const webSocket = setupWebSocket();
        return () => {
            webSocket?.close();
        };
    }, []);

    return (
        <Card className={"col-span-2 m-6 md:mr-3 sm:mb-3 h-fit"}>
            <CardBody>
                <div className={
                    "absolute top-0 right-0 mr-2 cursor-pointer text-gray-500"
                } onClick={() => {
                    setStopped(true);
                    window.streamClosed = true;
                    ws?.close();
                    props.close();
                }}>
                    x
                </div>

                <img
                    id="video"
                    src=""
                    alt="Video stream not available."
                    className={loading ? "opacity-0" : "z-20 rounded-2xl"}
                />
                {loading ? (
                    <div
                        className={"absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 mb-4"}
                    >
                        {stopped ? <FaPause/> : <Spinner/>}
                    </div>
                ) : (
                    <div className="flex items-center justify-center mt-4">
                        <span className="mr-2">FPS: {fps}</span>
                    </div>
                )}
                <div className={"flex justify-center mt-4"}>
                    {!stopped ?
                        <CustomButton
                            isIconOnly
                            color={"warning"}
                            aria-label={"Stop"}
                            className={"self-center"}
                            onClick={() => {
                                ws?.close();
                                setStopped(true);
                            }}
                        >
                            <FaPause/>
                        </CustomButton> :
                        <CustomButton
                            isIconOnly
                            color={"success"}
                            aria-label={"Unpause"}
                            className={"self-center"}
                            onClick={() => {
                                setupWebSocket();
                                setStopped(false);
                            }}
                        >
                            <FaPlay/>
                        </CustomButton>
                    }
                    <CustomButton
                        isIconOnly
                        color={"secondary"}
                        aria-label={"Restart"}
                        className={"ml-2 self-center"}
                        onClick={() => {
                            setStopped(false);
                            props.refresh();
                        }}
                    >
                        <FaArrowsRotate/>
                    </CustomButton>
                </div>
            </CardBody>
        </Card>
    );
};

export default LiveStream;
