import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class test {
    public String baseUrl ="http://the-internet.herokuapp.com";
    public WebDriver driver;
    public StringBuffer verificationErrors = new StringBuffer();

    @Before
    public void setUp() throws Exception {
        driver = new ChromeDriver();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(30,TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(30,TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }
    @Test
    public void Authorization() throws Exception {
        driver.get(baseUrl + "/");
        Thread.sleep(3000);
        MoveToElement(By.linkText("Form Authentication"),this.driver);
        driver.findElement(By.linkText("Form Authentication")).click();
        Thread.sleep(3000);
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys("tomsmith");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("SuperSecretPassword!");
        driver.findElement(By.className("radius")).click();
        Thread.sleep(4000);
        assertEquals(baseUrl+"/secure",driver.getCurrentUrl());
        try {
            ImageIO.write(grabScreen(), "png", new File(getHomeDir(), "screen.png"));
        } catch (IOException e) {
            System.out.println("IO exception"+e);
        }
    }
    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    public void MoveToElement(By by, WebDriver driver) {
        try {
            JavascriptExecutor jse = (JavascriptExecutor)driver;
            WebElement element = driver.findElement(by);
            jse.executeScript("arguments[0].scrollIntoView(true);", element);
            jse.executeScript("window.scrollBy(0,-250)", "");
        } catch (NoSuchElementException e) {}
    }
    private static File getHomeDir() {
        FileSystemView fsv = FileSystemView.getFileSystemView();
        return fsv.getHomeDirectory();
    }
    private static BufferedImage grabScreen() {
        try {
            return new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize())) ;
        } catch (SecurityException e) {
        } catch (AWTException e) {
        }
        return null;
    }
}
