import {MotorState} from "@/types/motor/state.ts";
import {GiCarWheel} from "react-icons/gi";

type MotorProps = {
    state: MotorState;
}

const Motor = (props: MotorProps) => {
    return (
        <div>
            <GiCarWheel className={
                "text-4xl " +
                (props.state.movementState !== 2 ? " text-green-500" : "") +
                (props.state.movementState == 0 ? " animate-rotate-forward" : "") +
                (props.state.movementState == 1 ? " animate-rotate-backward" : "")
            }/>
        </div>
    );
};

export default Motor;