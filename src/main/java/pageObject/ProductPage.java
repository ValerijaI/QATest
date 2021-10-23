package pageObject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.LocalWebDriver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;


public class ProductPage {

    private final WebDriver webDriver = LocalWebDriver.getInstance();
    private By addToCartElement = By.name("add-to-cart");
    private By message = By.className("woocommerce-message");
    private By shoppingCart = By.className("cart-contents");

    public ProductPage addProductToCart() {
        webDriver.findElement(addToCartElement).click();
        assertThat(webDriver.findElement(message).isDisplayed(), is(Boolean.TRUE));
        assertThat(webDriver.findElement(message).getText(), containsString("has been added to your cart"));
        return this;
    }

    public CartPage checkShoppingCart() {
        webDriver.findElement(shoppingCart).click();
        return new CartPage();
    }
}
