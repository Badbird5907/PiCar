import LiveStream from "@/components/live-stream.tsx";
import {useState} from "react";
import {Card, CardBody, Spinner} from "@nextui-org/react";

function Index() {
    const [showLiveStream, setShowLiveStream] = useState(true)
    return (
        <div>
            <h1 className={
                "text-4xl text-center text-foreground dark:text-foreground-dark font-bold"
            }>PiCar</h1>
            <a href={"/settings"} className={
                "text-2xl text-center text-foreground dark:text-foreground-dark font-bold"
            }>Settings</a>
            <div className={"grid grid-cols-1 md:grid-cols-8"}>
                {showLiveStream ?
                    <LiveStream refresh={() => {
                        setShowLiveStream(false)
                        setTimeout(() => {
                            setShowLiveStream(true)
                        }, 250)
                    }} /> :
                    <Card>
                        <CardBody>
                            <Spinner />
                        </CardBody>
                    </Card>
                }
            </div>
        </div>
    )
}

export default Index
