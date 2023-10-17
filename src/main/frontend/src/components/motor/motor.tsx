import {MotorState} from "@/types/motor/state.ts";
import {GiCarWheel} from "react-icons/gi";
import {useDynamicModal} from "@/components/dynamic-modal";
import {Button as NextUIButton} from "@nextui-org/button";
import CustomButton from "@/components/button";
import {Input} from "@nextui-org/react";
import apiClient from "@/util/api-client";
import {MovementState} from "@/types/motor/movement-state";
import {MotorSide} from "@/types/motor/motor-side";

type MotorProps = {
    state: MotorState;
}
export const showSpeedControl = (state: MotorState | number, { showModal, closeModal }: any) => {
    showModal({
        title: "Motor Properties",
        body: (
            <div className={"flex flex-col"}>
                <Input
                    type="number"
                    label="Speed"
                    id={"speed-input"}
                    placeholder={typeof state == "number" ? state.toString() : state.speed.toString()}
                    labelPlacement="outside"
                    min={0}
                    endContent={
                        <div className="pointer-events-none flex items-center">
                            <span className="text-default-400 text-small">%</span>
                        </div>
                    }
                />
            </div>
        ),
        footer: (
            <>
                <CustomButton
                    closeModal={closeModal}
                    onClickLoading={() => {
                        return new Promise((resolve, reject) => {
                            const speedInput = document.getElementById("speed-input") as HTMLInputElement;
                            if (!speedInput) {
                                reject();
                                return;
                            }
                            const speed = parseInt(speedInput.value);
                            if (isNaN(speed)) {
                                reject();
                                return;
                            }
                            const data: {
                                side: MotorSide | undefined;
                                speed: number;
                            } = {
                                side: undefined,
                                speed
                            };
                            if (typeof state != "number") {
                                data.side = state.side;
                            }
                            apiClient.post("/api/motor/speed", data).then(() => {
                                resolve("");
                            }).catch(() => {
                                reject();
                            });
                        })
                    }}
                >
                    Save
                </CustomButton>
                <NextUIButton
                    color={"danger"}
                    onPress={closeModal}
                >
                    Close
                </NextUIButton>
            </>
        )
    });
}
const Motor = (props: MotorProps) => {
    const {showModal, closeModal} = useDynamicModal();
    return (
        <div className={"hover:cursor-pointer hover:scale-110 transition"} onClick={() => {
            showSpeedControl(props.state, {showModal, closeModal});
        }}>
            <GiCarWheel className={
                "text-4xl " +
                (props.state.movementState !== MovementState.STOPPED ? " text-green-500" : "") +
                (props.state.movementState == MovementState.FORWARD ? " animate-rotate-forward" : "") +
                (props.state.movementState == MovementState.BACKWARD ? " animate-rotate-backward" : "")
            }/>
        </div>
    );
};

export default Motor;