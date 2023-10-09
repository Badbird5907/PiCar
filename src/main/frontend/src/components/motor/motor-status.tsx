import ReconnectingWebSocket from "reconnecting-websocket";
import {Card, CardBody, Skeleton} from "@nextui-org/react";
import {ReactElement, useEffect, useRef, useState} from "react";
import {MotorSide} from "@/types/motor/motor-side.ts";
import {MotorState} from "@/types/motor/state.ts";
import Motor from "@/components/motor/motor.tsx";
import {MovementDirection, MovementState} from "@/types/motor/movement-state.ts";
import MotorControl from "@/components/motor/motor-control.tsx";
import {FaArrowDown, FaArrowLeft, FaArrowRight, FaArrowUp} from "react-icons/fa";
import {FaCircleXmark} from "react-icons/fa6";

type MotorStatusProps = {
    ws: ReconnectingWebSocket | null;
}
const MotorStatus = (props: MotorStatusProps) => {
    const [motorStates, setMotorStates] = useState<MotorState[]>([]);
    const [icon, setIcon] = useState<ReactElement>(<FaCircleXmark />);
    const [_movementDirection, _setMovementDirection] = useState<number>(MovementDirection.STOPPED);
    const directionRef = useRef(_movementDirection);
    const setMovementDirection = (data: number) => {
        directionRef.current = data;
        _setMovementDirection(data);
    }
    const getIcon = () => {
        const movementDirection = directionRef.current;
        if (movementDirection === MovementDirection.LEFT) {
            return <FaArrowLeft className={"text-green-500"} />;
        } else if (movementDirection === MovementDirection.RIGHT) {
            return <FaArrowRight className={"text-green-500"} />;
        } else if (movementDirection === MovementDirection.FORWARD) {
            return <FaArrowUp className={"text-green-500"} />;
        } else if (movementDirection === MovementDirection.BACKWARD) {
            return <FaArrowDown className={"text-green-500"} />;
        }
        return <FaCircleXmark className={"text-red-500"} />;
    }
    useEffect(() => {
        if (!props.ws) return;
        const listener = (e: MessageEvent<any>) => {
            const json = JSON.parse(e.data);
            if (json.name !== "heartbeat") return;
            const motors: any = json.data.motors;
            const movementDirection = json.data.movementDirection;
            // @ts-ignore
            const dirEnum: number = MovementDirection[movementDirection as any];
            setMovementDirection(dirEnum);
            const keys = Object.keys(motors);
            const motorStates: MotorState[] = [];
            keys.forEach((key: string) => {
                const motor = motors[key];
                // @ts-ignore
                const motorSide: number = MotorSide[key as any];
                // @ts-ignore
                const movementState: number = MovementState[motor as any];
                motorStates.push({
                    side: motorSide,
                    movementState,
                });
            })
            setMotorStates(motorStates);
            setIcon(getIcon());
        }
        props.ws.addEventListener("message", listener);
        return () => {
            props.ws?.removeEventListener("message", listener);
        }
    }, [props.ws]);
    return (
        <div className={"col-span-2 m-6 md:mr-3 sm:mb-3 h-fit"}>
            <MotorControl ws={props.ws} />
            <Card>
                <CardBody>
                    {motorStates ?
                        <div className={"flex flex-row gap-4 justify-center"}>
                            {/*motorStates.map((motorState: MotorState) => {
                                return <Motor state={motorState}/>
                            })*/
                                // get half of the array
                                motorStates.slice(0, Math.ceil(motorStates.length / 2)).map((motorState: MotorState) => {
                                    return <Motor state={motorState}/>
                                })
                            }
                            <div className={"flex flex-col justify-center"}>
                                {icon}
                            </div>
                            {motorStates.slice(Math.ceil(motorStates.length / 2)).map((motorState: MotorState) => {
                                return <Motor state={motorState}/>
                            })}
                        </div> :
                        <>
                            <Skeleton className="rounded-lg">
                                <div className="h-24 rounded-lg bg-default-300"></div>
                            </Skeleton>
                        </>}
                </CardBody>
            </Card>
        </div>
    );
};

export default MotorStatus;