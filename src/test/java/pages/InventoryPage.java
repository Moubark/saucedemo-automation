package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page Object untuk halaman Inventory (daftar produk) setelah login berhasil.
 */
public class InventoryPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By pageTitle = By.className("title");
    private final By inventoryItems = By.className("inventory_item");
    private final By addToCartButtons = By.cssSelector("button[data-test^='add-to-cart']");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By sortDropdown = By.className("product_sort_container");
    private final By itemPrices = By.className("inventory_item_price");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isLoaded() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle))
                .getText().equalsIgnoreCase("Products");
    }

    public int getProductCount() {
        return driver.findElements(inventoryItems).size();
    }

    public void addFirstItemToCart() {
        driver.findElements(addToCartButtons).get(0).click();
    }

    public void addAllItemsToCart() {
        List<org.openqa.selenium.WebElement> buttons = driver.findElements(addToCartButtons);
        // Klik dari index terakhir supaya DOM re-render tidak bikin locator stale
        for (int i = buttons.size() - 1; i >= 0; i--) {
            driver.findElements(addToCartButtons).get(0).click();
        }
    }

    public int getCartBadgeCount() {
        if (driver.findElements(cartBadge).isEmpty()) return 0;
        return Integer.parseInt(driver.findElement(cartBadge).getText());
    }

    public void sortBy(String visibleText) {
        org.openqa.selenium.support.ui.Select select =
                new org.openqa.selenium.support.ui.Select(driver.findElement(sortDropdown));
        select.selectByVisibleText(visibleText);
    }

    public List<Double> getAllPrices() {
        return driver.findElements(itemPrices).stream()
                .map(el -> Double.parseDouble(el.getText().replace("$", "")))
                .toList();
    }
}
