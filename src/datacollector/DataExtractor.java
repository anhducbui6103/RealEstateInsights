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
    public JSONArray extractData(WebDriver driver, int targetCount) throws InterruptedException {
        JSONArray jsonArray = new JSONArray();
        int elementCount = 0;

        while (elementCount < targetCount) {
            List<WebElement> collectionElements = driver.findElements(By.xpath("//*[@id=\"product-lists-web\"]/div[*]"));

            for (WebElement element : collectionElements) {
                JSONObject nhaDatData = extractElementData(element);
                if (nhaDatData != null) {
                    jsonArray.add(nhaDatData);
                    elementCount++;
                }
            }

//            // Click next page button to collect more information
//            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//            WebElement nextPageButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[7]/div[2]/div[2]/div[7]/div/a[7]")));
//            nextPageButton.click();
//            Thread.sleep(5000); // Add a delay to allow the next page to load
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

            String cost = "Giá thỏa thuận";
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
