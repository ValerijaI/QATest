import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pageObject.CartPage;
import pageObject.MainPage;
import pageObject.ProductPage;
import utils.LocalWebDriver;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static utils.PropertiesReader.getProperties;

@Slf4j
public class CouponTest {

    private final WebDriver driver = LocalWebDriver.getInstance();
    private final MainPage mainPage = new MainPage();
    private final ProductPage productPage = new ProductPage();
    private final CartPage cartPage = new CartPage();

    @Test
    public void testCouponCodeNotAvailableOnMainPage() {
        driver.get(getProperties().getProperty("home.page"));
        By couponName = By.name("apply_coupon");
        assertThrows(NoSuchElementException.class, () -> driver.findElement(couponName));
    }

    @Test
    public void testCouponCodeNotAvailableOnProductPage() {
        driver.get(getProperties().getProperty("product.beanie.page"));
        By couponName = By.name("apply_coupon");
        assertThrows(NoSuchElementException.class, () -> driver.findElement(couponName));
    }

    @Test
    public void testCouponCodeNotAvailableOnCartPageIfCartIsEmpty() {
        By couponName = By.name("apply_coupon");
        driver.get(getProperties().getProperty("cart.page"));
        assertThrows(NoSuchElementException.class, () -> driver.findElement(couponName));
    }

    @Test
    public void testCouponCodeAvailableOnCartPage() {
        driver.get(getProperties().getProperty("home.page"));
        mainPage.selectProductFromListByName("Polo").addProductToCart();
        productPage.checkShoppingCart();
        By couponName = By.name("apply_coupon");
        Optional<WebElement> addCouponOnCartPage = Optional.of(driver.findElement(couponName));
        assertTrue(addCouponOnCartPage.isPresent());
    }

    @Test
    public void testNotValidCouponCode() {
        addPoloToCartAndOpenPoloProductPage();
        Double price = cartPage.getTotalPrice();

        enterCouponCode("strange_coupon");

        Double newPrice = cartPage.getTotalPrice();
        assertEquals(price, newPrice);
    }

    @Test
    public void testCouponCodeExpired() {
        addPoloToCartAndOpenPoloProductPage();
        Double price = cartPage.getTotalPrice();

        enterCouponCode("expired");

        waitSomeTime();

        Double newPrice = cartPage.getTotalPrice();
        assertEquals(price, newPrice);

        By message = By.className("woocommerce-error");
        assertTrue(driver.findElement(message).isDisplayed());
        assertThat(driver.findElement(message).getText(), containsString("This coupon has expired"));
    }

    @Test
    public void testIsPossibleToRemoveCouponCode() {
        addPoloToCartAndOpenPoloProductPage();
        Double price = cartPage.getTotalPrice();

        enterCouponCode("easy_discount");

        waitSomeTime();

        By couponAdded = By.className("cart-discount");
        Optional<WebElement> couponAddedOptional = Optional.of(driver.findElement(couponAdded));
        assertTrue(couponAddedOptional.isPresent());

        Double newPrice = cartPage.getTotalPrice();
        assertNotEquals(price, newPrice);

        By removeCouponCode = By.className("woocommerce-remove-coupon");
        driver.findElement(removeCouponCode).click();

        waitSomeTime();

        newPrice = cartPage.getTotalPrice();
        assertEquals(price, newPrice);
    }

    @Test
    public void testItIsPossibleToAddSeveralCouponCodes() {
        addPoloToCartAndOpenPoloProductPage();
        Double price = cartPage.getTotalPrice();

        enterCouponCode("easy_discount");

        waitSomeTime();

        By couponAdded = By.className("cart-discount");
        List<WebElement> webElementList = driver.findElements(couponAdded);
        assertTrue(webElementList.size() == 1);

        enterCouponCode("additional_discount");

        waitSomeTime();

        webElementList = driver.findElements(couponAdded);
        assertTrue(webElementList.size() == 2);

        Double newPrice = cartPage.getTotalPrice();
        assertNotEquals(price, newPrice);
    }

    @Test
    public void testItIsNotPossibleToAddSeveralAcodemyCouponCodes() {
        addPoloToCartAndOpenPoloProductPage();
        Double price = cartPage.getTotalPrice();

        enterCouponCode("acodemy10off");

        waitSomeTime();

        By couponAdded = By.className("cart-discount");
        List<WebElement> webElementList = driver.findElements(couponAdded);
        assertTrue(webElementList.size() == 1);

        enterCouponCode("acodemy20off");

        waitSomeTime();

        webElementList = driver.findElements(couponAdded);
        assertTrue(webElementList.size() == 1);

        Double newPrice = cartPage.getTotalPrice();
        assertNotEquals(price, newPrice);
    }

    @Test
    public void testAcodemyCouponCodesAreOverwrittenWithNew() {
        addPoloToCartAndOpenPoloProductPage();

        enterCouponCode("acodemy10off");

        waitSomeTime();

        By couponAdded = By.className("cart-discount");

        enterCouponCode("acodemy20off");

        waitSomeTime();

        List<WebElement> webElementList = driver.findElements(couponAdded);

        String couponName = webElementList.get(0).getText();
        assertTrue(couponName.contains("acodemy20off"));
    }

    private void waitSomeTime() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void enterCouponCode(String couponCode) {
        By enterCouponField = By.name("coupon_code");
        WebElement enterCoupon = driver.findElement(enterCouponField);
        enterCoupon.sendKeys(couponCode);

        By applyCouponButton = By.name("apply_coupon");
        WebElement applyCoupon = driver.findElement(applyCouponButton);
        applyCoupon.click();
    }

    private void addPoloToCartAndOpenPoloProductPage() {
        driver.get(getProperties().getProperty("home.page"));
        mainPage.selectProductFromListByName("Polo").addProductToCart();
        productPage.checkShoppingCart();
    }

    @AfterEach
    public void tearDown() {
        LocalWebDriver.closeDriver();
    }
}
