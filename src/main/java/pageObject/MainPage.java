package pageObject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.Objects;

public class MainPage {

    WebDriver webDriver;
    private final By productElement = By.xpath("//ul[contains(@class, 'products')]/li");

    @FindBy(xpath = "//ul[contains(@class, 'products')]/li")
    List<WebElement>elementList; // We can return in getAllProducts()

    public MainPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public List<WebElement> getAllProducts() {
        return webDriver.findElements(productElement);
    }
}
