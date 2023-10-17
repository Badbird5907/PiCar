import {MotorSide} from "@/types/motor/motor-side";
import {MovementState} from "@/types/motor/movement-state";

export type MotorState = {
    side: MotorSide;
    movementState: MovementState;
    speed: number;
}