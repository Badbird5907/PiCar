import CustomButton from "@/components/button";
import {FaArrowDown, FaArrowLeft, FaArrowRight, FaArrowUp} from "react-icons/fa";

const DirectionButton = ({direction, children, className}:
                             {
                                 direction: string,
                                 children?: React.ReactNode,
                                 className?: string
                             }) => {
    const sendDirection = (direction: string) => {
        if (!window.ws) {
            return;
        }
        window.ws.send(JSON.stringify({
            name: "motor",
            data: {
                state: direction
            }
        }));
    }
    return <CustomButton
        className={`w-fit ${className || ""}`}
        onPressStart={() => sendDirection(direction)}
        onPressEnd={() => sendDirection("stop")}
    >{children}</CustomButton>;
}
const MobileControl = () => {
    return (
        <div className={"w-full flex flex-col mt-4"}>
            <div className={"self-center"}>
                <DirectionButton direction={"w"}>
                    <FaArrowUp/>
                </DirectionButton>
            </div>
            <div className={"w-full"}>
                <DirectionButton direction={"a"} className={"left-0"}>
                    <FaArrowLeft/>
                </DirectionButton>
                <DirectionButton direction={"d"} className={"right-0 ml-auto float-right"}>
                    <FaArrowRight/>
                </DirectionButton>
            </div>
            <div className={"self-center"}>
                <DirectionButton direction={"s"}>
                    <FaArrowDown/>
                </DirectionButton>
            </div>
        </div>
    );
};

export default MobileControl;