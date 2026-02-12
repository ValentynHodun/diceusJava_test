package insiderone;

import base.BaseTest;
import com.codeborne.selenide.Condition;
import insiderone.testdata.IndustriesMenuItem;
import insiderone.testdata.PlatformMenuItem;
import insiderone.testdata.ResourcesMenuItem;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.MainPage;

import java.util.Map;

import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;

public class HomePageTests extends BaseTest {

    @BeforeClass
    public void openBasePageAndAcceptCookiesIfExist() {
        open(baseUrl);
        at(MainPage.class).acceptCookiesIfExist();
    }

    @Test(groups = "smoke")
    public void verifyHomePageContainsAllNeedInformation() {
        open(baseUrl);

        at(MainPage.class).getHeaderBlock().shouldBe(Condition.visible);
        at(MainPage.class).getSearchDemoFieldWithEmail().shouldBe(Condition.visible);

        at(MainPage.class).getDemoButton().shouldBe(Condition.visible)
                .shouldHave(Condition.attribute("href", "https://insiderone.com/request-a-demo/"))
                .shouldHave(Condition.text("Get a demo"));

        at(MainPage.class).getPlatformTourButton().shouldBe(Condition.visible)
                .shouldHave(Condition.attribute("href", "https://insiderone.com/product-demo-hub/"))
                .shouldHave(Condition.text("Platform Tour"));
    }

    @Test
    public void VerifyLinksInMenuBlock() {
        open(baseUrl);

        at(MainPage.class).getCustomersMenuItemAndVerifyLink()
                .shouldBe(Condition.visible)
                .shouldHave(Condition.attribute("href", "https://insiderone.com/customers/"));

        Map<String, String> platform = at(MainPage.class).navigateToMenuAndVarifyElements("Platform");
        assertThat(platform)
                .as("Platform menu should contain all expected items")
                .containsExactlyInAnyOrderEntriesOf(PlatformMenuItem.toMap());

        Map<String, String> industries = at(MainPage.class).navigateToMenuAndVarifyElements("Industries");
        assertThat(industries)
                .as("Industries menu should contain all expected items")
                .containsExactlyInAnyOrderEntriesOf(IndustriesMenuItem.toMap());

        Map<String, String> resources = at(MainPage.class).navigateToMenuAndVarifyElements("Resources");
        assertThat(resources)
                .as("Resources menu should contain all expected items")
                .containsExactlyInAnyOrderEntriesOf(ResourcesMenuItem.toMap());
    }


}
