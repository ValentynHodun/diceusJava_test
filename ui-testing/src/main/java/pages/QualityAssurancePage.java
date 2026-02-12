package pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byId;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class QualityAssurancePage {

    private final ElementsCollection departments = $$(".position-department");


    @Step
    public QualityAssurancePage clickSeeAllQaJobsButton() {
        $("a[href*='department=qualityassurance']").shouldBe(Condition.visible).click();
        return this;
    }

    @Step
    public QualityAssurancePage selectLocationsInDropDown(String location) {
        SelenideElement locationDropdown = $("#filter-by-location");

        locationDropdown.shouldBe(Condition.visible);
        locationDropdown.selectOption(location);

        return this;
    }

    @Step
    public SelenideElement getSelectedDepartmentInDropDown() {
        return $(byAttribute("name", "filter-by-department"));
    }

    @Step
    public void waitUntilOnlyQaPositionWIllDisplay() {
        departments.shouldBe(CollectionCondition.allMatch(
                "all departments should be Quality Assurance",
                el -> el.getText().equals("Quality Assurance")
        ), Duration.ofSeconds(10));
    }

    @Step
    public ElementsCollection getAllJobTitles() {
        return $$(".position-title");
    }

    @Step
    public ElementsCollection getAllJobsDepartment() {
        return $$(".position-department");
    }

    @Step
    public ElementsCollection getAllJobsLocation() {
        return $$(".position-location");
    }

    @Step
    public void openFirstJobApplicationPage() {
        $$(".position-list-item").first()
                .shouldBe(Condition.visible)
                .find("a.btn-navy")
                .click();
    }
}
