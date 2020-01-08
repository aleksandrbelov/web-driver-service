package com.detectify.codingchallenge.webdriverservice.service.impl;

import com.detectify.codingchallenge.webdriverservice.config.WebDriverProps;
import com.detectify.codingchallenge.webdriverservice.exception.TakingScreenShotException;
import com.detectify.codingchallenge.webdriverservice.service.WebDriverService;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class WebDriverServiceImpl implements WebDriverService {

    private WebDriver driver;

    private final String location;
    private final ChromeOptions options;

    @Autowired
    public WebDriverServiceImpl(WebDriverProps webDriverProps) {
        this.location = webDriverProps.getLocation();
        this.options = new ChromeOptions();
        this.options.addArguments(webDriverProps.getOptions());
    }

    @PostConstruct
    public void init() {
        System.setProperty("webdriver.chrome.driver", location);
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }

    @PreDestroy
    public void destroy() {
        driver.quit();
    }

    @Override
    public byte[] takeScreenShot(String url) {
        driver.get(url);

        Screenshot screenshot = takeScreenShotEntirePage();
        BufferedImage image = screenshot.getImage();

        return toByteArray(image);
    }

    private Screenshot takeScreenShotEntirePage() {
        return new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000))
                    .takeScreenshot(driver);
    }

    private byte[] toByteArray(BufferedImage image) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        write(image, output);
        return output.toByteArray();
    }

    private void write(BufferedImage image, ByteArrayOutputStream output) {
        try {
            ImageIO.write(image, "PNG", output);
        } catch (IOException e) {
            throw new TakingScreenShotException(e);
        }
    }
}
