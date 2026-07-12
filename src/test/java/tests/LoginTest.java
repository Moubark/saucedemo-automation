package tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.InventoryPage;
import pages.LoginPage;

public class LoginTest extends BaseTest {

    @Test(description = "Login dengan kredensial valid harus masuk ke halaman produk")
    public void testLoginSuccess() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        loginPage.login("standard_user", "secret_sauce");

        InventoryPage inventoryPage = new InventoryPage(driver);
        Assert.assertTrue(inventoryPage.isLoaded(), "Halaman produk gagal dimuat setelah login");
        Assert.assertTrue(inventoryPage.getProductCount() > 0, "Tidak ada produk yang ditampilkan");
    }

    @Test(description = "Login dengan password salah harus menampilkan error")
    public void testLoginWrongPassword() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        loginPage.login("standard_user", "salah_password");

        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error message tidak muncul untuk password salah");
        Assert.assertTrue(loginPage.getErrorText().contains("do not match"),
                "Pesan error tidak sesuai ekspektasi");
    }

    @Test(description = "Login dengan user yang di-lock harus ditolak")
    public void testLoginLockedUser() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        loginPage.login("locked_out_user", "secret_sauce");

        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error tidak muncul untuk locked user");
        Assert.assertTrue(loginPage.getErrorText().contains("locked out"),
                "Pesan error locked user tidak sesuai");
    }

    @Test(dataProvider = "invalidCredentials",
          description = "Login dengan berbagai kombinasi input kosong harus gagal")
    public void testLoginEmptyFields(String username, String password, String expectedErrorPart) {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        loginPage.login(username, password);

        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error tidak muncul untuk input tidak lengkap");
        Assert.assertTrue(loginPage.getErrorText().contains(expectedErrorPart),
                "Pesan error tidak sesuai untuk kombinasi: [" + username + ", " + password + "]");
    }

    @DataProvider(name = "invalidCredentials")
    public Object[][] invalidCredentials() {
        return new Object[][] {
                {"", "", "Username is required"},
                {"standard_user", "", "Password is required"},
                {"", "secret_sauce", "Username is required"},
        };
    }
}
