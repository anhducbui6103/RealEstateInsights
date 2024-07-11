package datacollector;

import org.json.simple.JSONArray;
import org.openqa.selenium.WebDriver;

public class DataCollector {
    public static void main(String[] args) {
        WebDriverManager webDriverManager = new WebDriverManager();
        WebDriver driver = webDriverManager.initDriver();

        try {
            driver.get("https://batdongsan.com.vn/nha-dat-ban");
            Thread.sleep(5000); // Wait for the page to load

            DataExtractor dataExtractor = new DataExtractor();
            JSONArray jsonArray = dataExtractor.extractData(driver, 100);

            JsonExporter jsonExporter = new JsonExporter();
            jsonExporter.exportJson(jsonArray, "datastorage/data.json");

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
