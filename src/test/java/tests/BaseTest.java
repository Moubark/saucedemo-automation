package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.ByteArrayInputStream;

/**
 * Base class untuk semua test.
 * Handle setup & teardown browser, plus auto-attach screenshot ke Allure
 * setiap kali ada test yang gagal — jadi begitu buka report, langsung
 * kelihatan kondisi halaman saat gagalnya tanpa perlu re-run.
 */
public class BaseTest {

    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        // Jalankan headless kalau di CI/CD (GitHub Actions), comment kalau mau lihat browser
        if (System.getenv("CI") != null) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
        }

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (driver != null) {
            if (result.getStatus() == ITestResult.FAILURE) {
                attachScreenshot("Screenshot saat gagal - " + result.getName());
            }
            driver.quit();
        }
    }

    /**
     * Ambil screenshot dan attach ke Allure report.
     * Nama method jadi judul attachment di report.
     */
    private void attachScreenshot(String name) {
        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        Allure.addAttachment(name, new ByteArrayInputStream(screenshot));
    }
}
