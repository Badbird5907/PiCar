import ReconnectingWebSocket from "reconnecting-websocket";
import {Card, CardBody, Skeleton} from "@nextui-org/react";
import {useEffect, useState} from "react";
import {MotorSide} from "@/types/motor/motor-side.ts";
import {MotorState} from "@/types/motor/state.ts";
import Motor from "@/components/motor/motor.tsx";
import {MovementState} from "@/types/motor/movement-state.ts";

type MotorStatusProps = {
    ws: ReconnectingWebSocket | null;
}
const MotorStatus = (props: MotorStatusProps) => {
    const [motorStates, setMotorStates] = useState<MotorState[]>([]);
    useEffect(() => {
        if (!props.ws) return;
        const listener = (e: MessageEvent<any>) => {
            const json = JSON.parse(e.data);
            if (json.name !== "heartbeat") return;
            const motors: any = json.data.motors;
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
        }
        props.ws.addEventListener("message", listener);
        return () => {
            props.ws?.removeEventListener("message", listener);
        }
    }, [props.ws]);
    return (
        <div className={"col-span-2 m-6 md:mr-3 sm:mb-3 h-fit"}>
            <Card>
                <CardBody>
                    {motorStates ?
                        <div className={"flex flex-row gap-4 justify-center"}>
                            {motorStates.map((motorState: MotorState) => {
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