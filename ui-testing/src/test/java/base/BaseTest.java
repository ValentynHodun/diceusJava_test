package base;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;

@ContextConfiguration(classes = {UITestConfig.class})
public class BaseTest extends AbstractTestNGSpringContextTests {

    @BeforeClass(alwaysRun = true)
    public void registerSelenideListener() {
        SelenideLogger.addListener("allureSelenide", new AllureSelenide().screenshots(true).savePageSource(false));
    }

    protected <T> T at(Class<T> aClass) {
        try {
            return aClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
