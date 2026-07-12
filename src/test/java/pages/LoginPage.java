package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object untuk halaman Login SauceDemo.
 * Setiap elemen & aksi di halaman ini dibungkus di sini,
 * supaya test class tidak perlu tahu detail locator.
 */
public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("h3[data-test='error']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Step("Buka halaman login SauceDemo")
    public void open() {
        driver.get("https://www.saucedemo.com/");
    }

    @Step("Login dengan username: {username}")
    public void login(String username, String password) {
        WebElement userEl = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
        userEl.clear();
        userEl.sendKeys(username);

        driver.findElement(passwordField).clear();
        driver.findElement(passwordField).sendKeys(password);

        driver.findElement(loginButton).click();
    }

    public boolean isErrorDisplayed() {
        return !driver.findElements(errorMessage).isEmpty();
    }

    public String getErrorText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
    }
}
