package datacollector;

import org.json.simple.JSONArray;
import org.openqa.selenium.WebDriver;

public class DataCollector {
    public static void main(String[] args) {
        WebDriverManager webDriverManager = new WebDriverManager();
        WebDriver driver = webDriverManager.initDriver();

        try {
            String baseUrl = "https://batdongsan.com.vn/nha-dat-ban";
            String nextPageUrl = baseUrl;
            int targetCount = 1000;
            int collectedCount = 0;

            while (collectedCount < targetCount) {
                driver = restartDriver(driver, webDriverManager); // Đóng và khởi tạo lại WebDriver
                driver.get(nextPageUrl);
                Thread.sleep(5000); // Wait for the page to load

                DataExtractor dataExtractor = new DataExtractor();
                JSONArray jsonArray = dataExtractor.extractData(driver);

                jsonArray.addAll(jsonArray);
                collectedCount += jsonArray.size();

                // Tạo URL cho trang tiếp theo
                int currentPage = getPageNumber(nextPageUrl);
                nextPageUrl = baseUrl + "/p" + (currentPage + 1);
                if (jsonArray.isEmpty()) {
                    break;
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    private static int getPageNumber(String url) {
        // Phương thức này để lấy số trang từ URL, ví dụ "/p2" sẽ trả về 2
        String[] parts = url.split("/p");
        if (parts.length > 1) {
            String pageNumber = parts[1].split("\\?")[0];
            return Integer.parseInt(pageNumber);
        }
        return 1; // Trang đầu tiên
    }

    public static WebDriver restartDriver(WebDriver driver, WebDriverManager webDriverManager) {
        driver.quit();
        return webDriverManager.initDriver();
    }

}