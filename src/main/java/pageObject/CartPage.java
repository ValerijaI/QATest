package pageObject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LocalWebDriver;

import java.util.List;

public class CartPage {

    private final WebDriver webDriver = LocalWebDriver.getInstance();
    private By price = By.className("woocommerce-Price-amount");

    public Double getTotalPrice() {
        List<WebElement> prices = webDriver.findElements(price);
        return getPriceAsDouble(prices.get(Math.max(0, prices.size() - 1)).getText());
    }

    private Double getPriceAsDouble(String priceString) {
        return Double.parseDouble(priceString.substring(1));
    }
}
