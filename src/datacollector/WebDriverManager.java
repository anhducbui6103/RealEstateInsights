package datacollector;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class WebDriverManager {
    public WebDriver initDriver() {
        System.setProperty("webdriver.edge.driver", "browserDrivers/msedgedriver.exe");
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--disable-extensions");
        options.addArguments("--start-maximized");
        options.addArguments("--force-device-scale-factor=1");
        return new EdgeDriver(options);
    }
}
