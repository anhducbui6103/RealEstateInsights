package datacollector;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import org.json.simple.JSONArray;

import java.io.FileWriter;
import java.io.IOException;

public class JsonExporter {
    public void exportJson(JSONArray jsonArray, String filePath) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String formattedJson = gson.toJson(JsonParser.parseString(jsonArray.toString()));

        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(formattedJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
