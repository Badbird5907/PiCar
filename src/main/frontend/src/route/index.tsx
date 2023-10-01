import LiveStream from "@/components/live-stream.tsx";
import {useState} from "react";
import {Card, CardBody, Spinner} from "@nextui-org/react";
import {FaCog} from "react-icons/fa";
import CustomButton from "@/components/button.tsx";
import Led from "@/components/test/led.tsx";

function Index() {
    const [showLiveStream, setShowLiveStream] = useState(0) // 0 = true, 1 = loading, 2 = false
    return (
        <div>
            <h1 className={
                "text-4xl text-center text-foreground dark:text-foreground-dark font-bold"
            }>PiCar</h1>
            <a href={"/settings"}>
                <CustomButton isIconOnly variant={"faded"} className={"right-0 top-0 absolute mr-2 mt-2"}>
                    <FaCog />
                </CustomButton>
            </a>
            <div className={"grid grid-cols-1 md:grid-cols-8"}>
                {showLiveStream == 0 ?
                    // TODO some kind of component to wrap all of these neatly
                    // works fine for now though
                    <LiveStream refresh={() => {
                        setShowLiveStream(1)
                        setTimeout(() => {
                            setShowLiveStream(0)
                        }, 250)
                    }} close={() => setShowLiveStream(2)} /> :
                    showLiveStream == 1 &&
                        <Card>
                            <CardBody>
                                <Spinner />
                            </CardBody>
                        </Card>
                }
                <Led/>
            </div>
        </div>
    )
}

export default Index
