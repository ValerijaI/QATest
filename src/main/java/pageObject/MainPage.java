package pageObject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.LocalWebDriver;

import java.util.List;

public class MainPage {

    WebDriver webDriver = LocalWebDriver.getInstance();
    private final By productElements = By.xpath("//ul[contains(@class, 'products')]/li");

    @FindBy(xpath = "//ul[contains(@class, 'products')]/li")
    List<WebElement> elementList; // We can return in getAllProducts()

    public List<WebElement> getElementList() {
        return webDriver.findElements(productElements);
    }

    public ProductPage selectProductFromListByName(String productName) {
        getElementList().stream()
                .filter(name -> name.getText().contains(productName))
                .findFirst()
                .ifPresent(WebElement::click);
        return new ProductPage();
    }
}
