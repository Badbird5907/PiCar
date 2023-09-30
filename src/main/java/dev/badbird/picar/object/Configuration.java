package dev.badbird.picar.object;

import dev.badbird.picar.PiCar;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.File;
import java.nio.file.Files;

@Data
public class Configuration {
    private int port = 1337;
    @SneakyThrows
    public void save() {
        String json = PiCar.getGson().toJson(this);
        // save json to config.json
        File config = new File("config.json");
        if (!config.exists()) {
            config.createNewFile();
        }
        Files.write(config.toPath(), json.getBytes());
    }
}
