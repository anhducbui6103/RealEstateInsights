package datacollector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class DataExtractor {
    public JSONArray extractData(WebDriver driver) throws InterruptedException {
        JSONArray jsonArray = new JSONArray();

        List<WebElement> collectionElements = driver.findElements(By.xpath("//*[@id=\"product-lists-web\"]/div[*]"));

        for (WebElement element : collectionElements) {
            JSONObject nhaDatData = extractElementData(element);
            if (nhaDatData != null) {
                jsonArray.add(nhaDatData);
            }
        }

        return jsonArray;
    }

    private JSONObject extractElementData(WebElement element) {
        try {
            WebElement titleElement = element.findElement(By.xpath("./a/div[2]/div[1]/h3"));

            String title = titleElement.getText();
            String info = element.findElement(By.xpath("./a/div[2]/div[1]/div[1]/div[1]")).getText();
            String address = element.findElement(By.xpath("./a/div[2]/div[1]/div[1]/div[2]/span[2]")).getText();
            String[] infoParts = info.split("\\n");

            String cost = "Giá thỏa thận";
            String area = "";
            String costPerM2 = "";
            String bedrooms = "";
            String bathrooms = "";

            if (infoParts.length >= 3) {
                area = infoParts[2].trim();
            }

            if (infoParts.length >= 5) {
                cost = infoParts[0].trim();
                costPerM2 = infoParts[4].trim();
            }

            if (infoParts.length >= 9) {
                bedrooms = infoParts[6].trim();
                bathrooms = infoParts[8].trim();
            }

            JSONObject nhaDatData = new JSONObject();
            nhaDatData.put("Title", title);
            nhaDatData.put("Address", address);
            nhaDatData.put("Total Cost", cost);
            nhaDatData.put("Area", area);
            nhaDatData.put("Cost Per M2", costPerM2.isEmpty() ? null : costPerM2);
            nhaDatData.put("Bedrooms", bedrooms.isEmpty() ? null : bedrooms);
            nhaDatData.put("Bathrooms", bathrooms.isEmpty() ? null : bathrooms);

            return nhaDatData;
        } catch (NoSuchElementException e) {
            return null;
        }
    }
}
