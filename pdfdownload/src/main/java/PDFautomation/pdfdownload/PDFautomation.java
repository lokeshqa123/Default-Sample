package PDFautomation.pdfdownload;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PDFautomation {

    private static final String BASE_URL = "https://ippne.truchart.com/truchart_app/Home.jsp";
    private static final String USERNAME = "CDWadmin1";
    private static final String PASSWORD = "CDWadmin1";

    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");

        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));

        try {
            driver.get(BASE_URL);

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tru_username"))).sendKeys(USERNAME);
            driver.findElement(By.id("j_password")).sendKeys(PASSWORD);
            driver.findElement(By.cssSelector("button.ui-button-text-only[type='submit']")).click();

            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.single-search"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.advanced-search"))).click();

            WebElement membership = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("form[name='form_patientSearch'] select[name='membershipstate']")));
            new Select(membership).selectByVisibleText("All");

            wait.until(ExpectedConditions.elementToBeClickable(By.id("FindButton"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("table.tablesorter tbody tr"))).click();

            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[@class='quick-tool-desc' and normalize-space()='CarePlan']"))).click();

            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'content_border_inner')]//span[text()='PDF']/.."))).click();

            switchToNewestWindow(driver);

            for (int step = 0; step < 5; step++) {
                if (!driver.findElements(By.id("PDFButton")).isEmpty()) {
                    break;
                }
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[span[text()='Next']]"))).click();
            }

            wait.until(ExpectedConditions.elementToBeClickable(By.id("PDFButton"))).click();

            switchToNewestWindow(driver); // processing pop-up
            wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("processtextinner"), "Done"));
            System.out.println("Reached Finish PDF; download initiated.");

        } finally {
            driver.quit();
        }
    }

    private static void switchToNewestWindow(WebDriver driver) {
        for (String handle : driver.getWindowHandles()) {
            driver.switchTo().window(handle);
        }
    }
}