package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {

    @Step
    public MainPage acceptCookiesIfExist() {
        SelenideElement acceptButton = $(byAttribute("data-cli_action", "accept_all"));
        if (acceptButton.is(Condition.visible) && acceptButton.is(Condition.enabled)) {
            acceptButton.click();
        }
        return this;
    }

    @Step
    public SelenideElement getSearchDemoFieldWithEmail() {
        return $(byId("email"));
    }

    @Step
    public SelenideElement getDemoButton() {
        return $("a.btn.btn-primary[href='/request-a-demo/']");
    }

    @Step
    public SelenideElement getPlatformTourButton() {
        return $("a.btn.btn-light[href='/product-demo-hub/']");
    }

    @Step
    public SelenideElement getHeaderBlock() {
        return $(".header-wrapper");
    }

    @Step
    public SelenideElement getCustomersMenuItemAndVerifyLink() {
        return $("a[data-text='Customers']");
    }

    @Step
    public Map<String, String> navigateToMenuAndVarifyElements(String menuName) {
        SelenideElement menuItem = $("button[data-text='" + menuName + "']")
                .closest(".header-menu-item");

        menuItem.$("button").hover();

        SelenideElement dropdown = menuItem.$(".header-menu-item-dropdown");
        dropdown.should(Condition.visible);

        Map<String, String> menuLinks = new HashMap<>();

        ElementsCollection links = dropdown.$$("a");
        links.forEach(link -> {
            String text = link.$("span").attr("data-text");
            String url = link.attr("href");
            if (text != null && url != null) {
                menuLinks.put(text, url);
            }
        });

        return menuLinks;
    }

}
