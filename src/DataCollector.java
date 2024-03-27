import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class DataCollector {
    public static void main(String[] args) {
        // Open browser
        System.setProperty("webdriver.edge.driver", "browserDrivers/msedgedriver.exe");
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--disable-extensions");
        options.addArguments("--start-maximized");
        options.addArguments("--force-device-scale-factor=1");
        WebDriver driver = new EdgeDriver(options);

        try{
            // Open Web
            driver.get("https://www.nhatot.com/mua-ban-bat-dong-san-ha-noi");
            Thread.sleep(5000);

            // Data Collect
            JSONArray jsonArray = new JSONArray();
            List<WebElement> collectionElements = driver.findElements(By.xpath("//*[@id=\"__next\"]/div/div[4]/div[1]/div[2]/main/div[1]/div[4]/div/div[1]/ul/div[*]"));

            for (WebElement element : collectionElements) {
                String title = element.findElement(By.xpath("./li/a/div[2]/div/h3")).getText();
                String info = element.findElement(By.xpath("./li/a/div[2]/div/div[1]")).getText();
                String price = element.findElement(By.xpath("./li/a/div[2]/div/div[2]/div/p")).getText();

                String[] infoParts = info.split(" - ");
                String area = "";
                String rooms = "";

                if (infoParts.length >= 2) {
                    area = infoParts[0].trim();
                    rooms = infoParts[1].trim();
                }

                String[] priceParts = price.split(" \\| ");
                String cost = "";
                String costPerM2 = "";

                if (priceParts.length >= 2) {
                    cost = priceParts[0].trim();
                    costPerM2 = priceParts[1].trim();
                }

                // Create JSON Object
                JSONObject nhaDatData = new JSONObject();
                nhaDatData.put("Title", title);
                nhaDatData.put("Area", area);
                nhaDatData.put("Rooms", rooms);
                nhaDatData.put("Total Cost", cost);
                nhaDatData.put("Cost Per M2", costPerM2);

                jsonArray.add(nhaDatData);

                // Export JSON data
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String formattedJson = gson.toJson(JsonParser.parseString(jsonArray.toString()));
                try (FileWriter fileWriter = new FileWriter("datastorage/data.json")) {
                    fileWriter.write(formattedJson);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            driver.quit();
        }
    }
}
