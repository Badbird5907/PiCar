import { useEffect, useState, useRef } from "react";
import { getSettingValue } from "@/util/setting.ts";
import { Button, Card, CardBody, Spinner } from "@nextui-org/react";
import {FaPause, FaPlay} from "react-icons/fa";
import {FaArrowsRotate} from "react-icons/fa6";

type LiveStreamProps = {
    refresh: () => void
}
const LiveStream = (props: LiveStreamProps) => {
    const [loading, setLoading] = useState(true);
    const [fps, setFps] = useState(0);
    const [ws, setWs] = useState<WebSocket | undefined>(undefined);
    const lastFrameTime = useRef(0);
    const [stopped, setStopped] = useState(false);

    const setupWebSocket = () => {
        const video = document.getElementById("video") as HTMLVideoElement;

        const webSocket = new WebSocket(getSettingValue("streamWs"));
        setWs(webSocket);
        console.log("Connecting to WebSocket...");
        webSocket.addEventListener("open", (_) => {
            console.log("WebSocket connection opened.");
            setLoading(false);
            lastFrameTime.current = performance.now();
        });
        webSocket.addEventListener("message", (event) => {
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
            console.error("WebSocket error:", event);
        });
        webSocket.addEventListener("close", (_) => {
            console.log("WebSocket connection closed.");
            setLoading(true);
            setTimeout(function() {
                console.log("Reconnecting...")
                setupWebSocket();
            }, 1000);
        });
        return webSocket;
    }

    useEffect(() => {
        const webSocket = setupWebSocket();
        return () => {
            webSocket.close();
        };
    }, []);

    return (
        <Card className={"col-span-2 m-6 md:mr-3 sm:mb-3 h-fit"}>
            <CardBody>
                <img
                    id="video"
                    src=""
                    alt="Video stream not available."
                    className={loading ? "opacity-0" : ""}
                />
                {loading ? (
                    <div
                        className={"absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 mb-4"}
                    >
                        {stopped ? <FaPause /> : <Spinner />}
                    </div>
                ) : (
                    <div className="flex items-center justify-center mt-4">
                        <span className="mr-2">FPS: {fps}</span>
                    </div>
                )}
                <div className={"flex justify-center mt-4"}>
                    {!stopped ?
                        <Button
                            isIconOnly
                            color={"warning"}
                            aria-label={"Stop"}
                            className={"self-center"}
                            onClick={() => {
                                ws?.close();
                                setStopped(true);
                            }}
                        >
                            <FaPause />
                        </Button> :
                        <Button
                            isIconOnly
                            color={"success"}
                            aria-label={"Unpause"}
                            className={"self-center"}
                            onClick={() => {
                                setupWebSocket();
                                setStopped(false);
                            }}
                        >
                            <FaPlay />
                        </Button>
                    }
                    <Button
                        isIconOnly
                        color={"secondary"}
                        aria-label={"Restart"}
                        className={"ml-2 self-center"}
                        onClick={() => {
                            setStopped(false);
                            props.refresh();
                        }}
                    >
                        <FaArrowsRotate />
                    </Button>
                </div>
            </CardBody>
        </Card>
    );
};

export default LiveStream;
