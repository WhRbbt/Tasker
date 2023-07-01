package tasker;

import com.google.gson.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Gson {
    private final com.google.gson.Gson gson;

    public Gson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        gsonBuilder.setPrettyPrinting();
        this.gson = gsonBuilder.create();
    }

    public void writeDataToFile(List<Task> tasks) {
        try {
            Files.writeString(Path.of("data.json"), gson.toJson(tasks), StandardOpenOption.CREATE);
        } catch (IOException e) {
            System.out.println("Error writing data to file: " + e.getMessage());
        }
    }

    public List<Task> readDataFromFile() {
        List<Task> tasks = new ArrayList<>();
        try {
            String json = Files.readString(Path.of("data.json"));
            Task[] taskArray = gson.fromJson(json, Task[].class);
            tasks = new ArrayList<>(Arrays.asList(taskArray));
        } catch (IOException e) {
            System.out.println("Error reading data from file: " + e.getMessage());
        }
        return tasks;
    }

    private static class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        @Override
        public JsonElement serialize(LocalDate date, Type type, JsonSerializationContext context) {
            return new JsonPrimitive(formatter.format(date));
        }

        @Override
        public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
            String dateString = json.getAsString();
            return LocalDate.parse(dateString, formatter);
        }
    }
}
