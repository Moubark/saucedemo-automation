package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.InventoryPage;
import pages.LoginPage;

public class CartTest extends BaseTest {

    private InventoryPage inventoryPage;

    @BeforeMethod
    public void loginFirst() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        loginPage.login("standard_user", "secret_sauce");
        inventoryPage = new InventoryPage(driver);
    }

    @Test(description = "Menambah 1 produk ke cart harus mengupdate badge cart jadi 1")
    public void testAddSingleItemToCart() {
        inventoryPage.addFirstItemToCart();
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 1,
                "Badge cart tidak menunjukkan 1 item setelah add to cart");
    }

    @Test(description = "Sorting produk by 'Price (low to high)' harus mengurutkan harga ascending")
    public void testSortPriceLowToHigh() {
        inventoryPage.sortBy("Price (low to high)");
        var prices = inventoryPage.getAllPrices();

        for (int i = 0; i < prices.size() - 1; i++) {
            Assert.assertTrue(prices.get(i) <= prices.get(i + 1),
                    "Urutan harga tidak ascending pada index " + i);
        }
    }
}
