package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.Duration;
import java.time.LocalDateTime;

public class GsonUtilLTA {

    public static Gson createGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }
}
