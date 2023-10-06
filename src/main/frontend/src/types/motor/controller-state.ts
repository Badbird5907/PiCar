import {MotorState} from "@/types/motor/state.ts";

export type MotorControllerState = {
    motors: MotorState[];
    speed: number;
}