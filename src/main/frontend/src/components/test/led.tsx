import {Card, CardBody} from "@nextui-org/react";
import useSWR from "swr";
import CustomButton from "@/components/button.tsx";
import axios from "axios";
import {getSettingValue} from "@/util/setting.ts";

const Led = () => {
    const { data, mutate, error, isLoading } = useSWR("/api/led/")
    return (
        <Card className={"col-span-2 m-6 md:mr-3 sm:mb-3 h-fit"}>
            <CardBody>
                {isLoading ? "Loading..." : data.state ? "LED is on" : "LED is off"}
                {error && "Error!"}
                <CustomButton onClickLoading={async () => {
                    const backend = getSettingValue("apiUrl");
                    await axios.post(`${backend}/api/led/toggle`).then(_ => {
                        mutate();
                    });
                }}>Toggle</CustomButton>
            </CardBody>
        </Card>
    );
};

export default Led;