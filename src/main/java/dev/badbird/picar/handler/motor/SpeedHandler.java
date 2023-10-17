package dev.badbird.picar.handler.motor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.badbird.picar.handler.APIRoute;
import dev.badbird.picar.motor.MotorSide;
import dev.badbird.picar.platform.Platform;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HandlerType;
import org.jetbrains.annotations.NotNull;

@APIRoute(value = "/api/motor/speed", type = HandlerType.POST)
public class SpeedHandler implements Handler {
    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        JsonObject json = JsonParser.parseString(ctx.body()).getAsJsonObject();
        int speed = json.get("speed").getAsInt();
        System.out.println("Setting speed to " + speed);
        if (!json.has("side")) {
            Platform.getPlatform().getMotorController().setSpeed(speed);
            ctx.result("{\"success\":true}");
            return;
        }
        MotorSide side = MotorSide.valueOf(json.get("side").getAsString());
        Platform.getPlatform().getMotorController().setSpeed(side, speed);
        ctx.result("{\"success\":true}");
    }
}
