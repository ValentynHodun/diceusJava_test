package insiderone;

import base.BaseTest;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.LeverApplicationPage;
import pages.MainPage;
import pages.QualityAssurancePage;

import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.switchTo;

public class QualityAssurancePageTests extends BaseTest {

    @BeforeClass
    public void openBasePageAndAcceptCookiesIfExist() {
        open( baseUrl + "careers/quality-assurance/");
        at(MainPage.class).acceptCookiesIfExist();
    }

    @Test(groups = "smoke")
    public void checkQualityAssurancePage() {
        openQaPageAndSelectFilterWithLocation("Istanbul, Turkiye");

        at(QualityAssurancePage.class).getAllJobsDepartment()
                .shouldHave(CollectionCondition.allMatch(
                        "all departments should be Quality Assurance",
                        el -> el.getText().equals("Quality Assurance")
                ));

        at(QualityAssurancePage.class).getAllJobsLocation()
                .shouldHave(CollectionCondition.allMatch(
                        "all locations should be Istanbul, Turkiye",
                        el -> el.getText().equals("Istanbul, Turkiye")
                ));

        at(QualityAssurancePage.class).getAllJobTitles()
                .shouldHave(CollectionCondition.allMatch(
                "all job title should be Quality Assurance",
                el -> el.getText().equals("Quality Assurance")
        ));
    }

    @Test
    public void checkThatApplicationPageCorrectlyOpen() {
        openQaPageAndSelectFilterWithLocation("Istanbul, Turkiye");

        at(QualityAssurancePage.class).openFirstJobApplicationPage();
        switchTo().window(1);

        at(LeverApplicationPage.class).applyForJobButton().shouldBe(Condition.visible);
    }

    private void openQaPageAndSelectFilterWithLocation(String location) {
        open(baseUrl + "careers/quality-assurance/");

        at(QualityAssurancePage.class).clickSeeAllQaJobsButton()
                .selectLocationsInDropDown(location);

        at(QualityAssurancePage.class).getSelectedDepartmentInDropDown().shouldHave(Condition.text("Quality Assurance"));
        at(QualityAssurancePage.class).waitUntilOnlyQaPositionWIllDisplay();
    }

    }
