package base;

import com.codeborne.selenide.FileDownloadMode;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;

@Configuration
@PropertySource({"classpath:environment/${target}/application.properties"})
public class UITestConfig {

    @Autowired
    Environment environment;

    @Bean
    public void configureBrowser() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName(environment.getProperty("browser.name"));
        capabilities.setVersion(environment.getProperty("browser.version"));
        capabilities.setCapability("selenoid:options", new HashMap<String, Object>() {{
            put("name", LocalDateTime.now().toString());
            put("env", Collections.singletonList("LANG=en_US.UTF-8"));
//          put("enableVNC", true);
//          put("enableVideo", true);
            put("sessionTimeout", "6m");
            put("timeZone", "Europe/Kyiv");
        }});
//      com.codeborne.selenide.Configuration.remote = environment.getProperty("selenoid.url");
        com.codeborne.selenide.Configuration.browser = "chrome";
        com.codeborne.selenide.Configuration.baseUrl = environment.getProperty("ui.url.dev");
        com.codeborne.selenide.Configuration.timeout = Long.parseLong(environment.getProperty("timeout"));
        com.codeborne.selenide.Configuration.browserSize = environment.getProperty("browser.size");
        com.codeborne.selenide.Configuration.headless = Boolean.parseBoolean(environment.getProperty("browser.headless"));
        com.codeborne.selenide.Configuration.holdBrowserOpen = Boolean.parseBoolean(environment.getProperty("browser.hold.open"));
        com.codeborne.selenide.Configuration.proxyEnabled = false;
        com.codeborne.selenide.Configuration.fileDownload = FileDownloadMode.FOLDER;
        com.codeborne.selenide.Configuration.browserCapabilities = capabilities;
    }
}
