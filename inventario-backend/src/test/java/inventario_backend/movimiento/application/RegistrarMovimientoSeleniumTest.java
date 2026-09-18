package inventario_backend.movimiento.application;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RegistrarMovimientoSeleniumTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    private void iniciarSesion() {
        driver.get("http://localhost:4200/login");
        WebElement txtUsername = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.name("username")));
        driver.findElement(By.name("password")).sendKeys("1234");
        txtUsername.sendKeys("admin");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    @Test
    void automatizacionCP001_RegistroEntradaExitosa() throws InterruptedException {
        iniciarSesion();
        Thread.sleep(1500);
        driver.get("http://localhost:4200/movimientos");

        WebElement selectProducto = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("select[formControlName='productoId']")));
        new Select(selectProducto).selectByIndex(1);

        WebElement selectTipo = driver.findElement(
                By.cssSelector("select[formControlName='tipo']"));
        new Select(selectTipo).selectByValue("ENTRADA");

        WebElement txtCantidad = driver.findElement(
                By.cssSelector("input[formControlName='cantidad']"));
        txtCantidad.clear();
        txtCantidad.sendKeys("5");

        Thread.sleep(6000);

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(".text-emerald-700")));

        Thread.sleep(6000);

        WebElement mensajeExito = driver.findElement(
                By.cssSelector(".text-emerald-700"));

        assertTrue(mensajeExito.getText().length() > 0);
    }

    @Test
    void automatizacionCP002_BloqueoCantidadNegativa() throws InterruptedException {
        iniciarSesion();
        Thread.sleep(1500);
        driver.get("http://localhost:4200/movimientos");

        WebElement selectProducto = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("select[formControlName='productoId']")));
        new Select(selectProducto).selectByIndex(1);

        WebElement selectTipo = driver.findElement(
                By.cssSelector("select[formControlName='tipo']"));
        new Select(selectTipo).selectByValue("ENTRADA");

        WebElement txtCantidad = driver.findElement(
                By.cssSelector("input[formControlName='cantidad']"));
        txtCantidad.clear();
        txtCantidad.sendKeys("-2");

        WebElement btnSubmit = driver.findElement(
                By.cssSelector("button[type='submit']"));

        Thread.sleep(6000);

        assertTrue(
                !btnSubmit.isEnabled(),
                "El botón de guardar debería estar deshabilitado para cantidades negativas");
    }

    @Test
    void automatizacionCP003_RegistroSalidaExitosa() throws InterruptedException {
        iniciarSesion();
        Thread.sleep(1500);
        driver.get("http://localhost:4200/movimientos");

        WebElement selectProducto = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("select[formControlName='productoId']")));
        new Select(selectProducto).selectByIndex(2);

        WebElement selectTipo = driver.findElement(
                By.cssSelector("select[formControlName='tipo']"));
        new Select(selectTipo).selectByValue("SALIDA");

        WebElement txtCantidad = driver.findElement(
                By.cssSelector("input[formControlName='cantidad']"));
        txtCantidad.clear();
        txtCantidad.sendKeys("2");

        Thread.sleep(6000);

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(".text-emerald-700")));

        Thread.sleep(6000);

        WebElement mensajeExito = driver.findElement(
                By.cssSelector(".text-emerald-700"));

        assertTrue(mensajeExito.getText().length() > 0);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}