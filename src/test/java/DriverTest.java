import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import pageObject.MainPage;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.PropertiesReader.getProperties;


public class DriverTest {

    private static WebDriver driver;
    MainPage mainPage;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
        driver = new ChromeDriver();
        driver.get(getProperties().getProperty("home.page"));
        mainPage = new MainPage(driver);
    }

    @Test
    public void testIfTitleIsCorrect() {
        assertThat(driver.getTitle(), equalTo("Online shop – acodemy – Just another WordPress site"));
    }

    @Test
    public void testWebElementSize() {
        List<WebElement> elementList = mainPage.getAllProducts();
        assertEquals(elementList.size(), 12);
    }


    @AfterEach
    public void tearDown() {
        driver.quit();
    }
}
