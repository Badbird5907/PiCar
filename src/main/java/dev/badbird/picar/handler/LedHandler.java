package dev.badbird.picar.handler;

import com.google.gson.JsonObject;
import dev.badbird.picar.PiCar;
import dev.badbird.picar.system.Platform;
import io.javalin.Javalin;

public class LedHandler {
    public static void init(Javalin app) {
        app.get("/api/led", ctx -> {
            JsonObject object = new JsonObject();
            object.addProperty("state", Platform.getPlatform().getLedState());
            ctx
                    .contentType("application/json")
                    .result(PiCar.getGson().toJson(object));
        });
        app.post("/api/led", ctx -> {
            JsonObject object = PiCar.getGson().fromJson(ctx.body(), JsonObject.class);
            Platform.getPlatform().setLedState(object.get("state").getAsBoolean());
            ctx.result("{\"success\": true}");
        });
        app.post("/api/led/toggle", ctx -> {
            Platform.getPlatform().setLedState(!Platform.getPlatform().getLedState());
            ctx.result("{\"state\": " + Platform.getPlatform().getLedState() + "}");
        });
    }

}
