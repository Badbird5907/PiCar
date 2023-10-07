import LiveStream from "@/components/live-stream.tsx";
import {useEffect, useState} from "react";
import {Card, CardBody, Spinner} from "@nextui-org/react";
import {FaCog} from "react-icons/fa";
import CustomButton from "@/components/button.tsx";
import ErrorBoundary from "@/components/error-boundary";
import WebsocketHandler from "@/util/websocket-handler.ts";
import MotorStatus from "@/components/motor/motor-status.tsx";
import ReconnectingWebSocket from "reconnecting-websocket";

function Index() {
    const [showLiveStream, setShowLiveStream] = useState(0) // 0 = true, 1 = loading, 2 = false
    const [ws, setWs] = useState<ReconnectingWebSocket | null>(null);
    useEffect(() => {
        setWs(WebsocketHandler.init());
        return () => {
            WebsocketHandler.close();
        }
    }, []);

    return (
        <div>
            <h1 className={
                "text-4xl text-center text-foreground dark:text-foreground-dark font-bold"
            }>PiCar</h1>
            <a href={"/settings"}>
                <CustomButton isIconOnly variant={"faded"} className={"right-0 top-0 absolute mr-2 mt-2"}>
                    <FaCog/>
                </CustomButton>
            </a>
            <div className={"grid grid-cols-1 md:grid-cols-8"}>
                {showLiveStream == 0 ?
                    // TODO some kind of component to wrap all of these neatly
                    // works fine for now though
                    <ErrorBoundary>
                        <LiveStream refresh={() => {
                            setShowLiveStream(1)
                            setTimeout(() => {
                                setShowLiveStream(0)
                            }, 250)
                        }} close={() => setShowLiveStream(2)}/>
                    </ErrorBoundary> :
                    showLiveStream == 1 &&
                    <Card>
                        <CardBody>
                            <Spinner/>
                        </CardBody>
                    </Card>
                }
                <MotorStatus ws={ws} />
            </div>
        </div>
    )
}

export default Index
