package insiderone.testdata;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum PlatformMenuItem {

    PLATFORM_OVERVIEW("Platform Overview", "https://insiderone.com/platform/"),
    INTEGRATION_HUB("Integration Hub", "https://insiderone.com/integrations/"),
    SIRIUS_AI("Sirius AI™", "https://insiderone.com/ai-overview/"),
    CUSTOMER_DATA_MANAGEMENT("Customer Data Management", "https://insiderone.com/customer-data-management/"),
    PERSONALIZATION("Personalization", "https://insiderone.com/ai-personalization/"),
    JOURNEY_ORCHESTRATION("Journey Orchestration", "https://insiderone.com/customer-journey/orchestration/"),
    REPORTING_DATA("Reporting & Data", "https://insiderone.com/reporting-analytics/"),
    BEHAVIORAL_ANALYTICS("Behavioral Analytics", "https://insiderone.com/behavioral-analytics/"),
    WEB("Web", "https://insiderone.com/channels/web/"),
    EMAIL("Email", "https://insiderone.com/channels/email/"),
    SITE_SEARCH("Site Search", "https://insiderone.com/eureka-search/"),
    CONVERSATIONAL_CX("Conversational CX", "https://insiderone.com/conversational-cx/"),
    WHATSAPP("WhatsApp", "https://insiderone.com/channels/whatsapp/"),
    WEB_PUSH("Web Push", "https://insiderone.com/channels/web-push/"),
    INSTORY("InStory", "https://insiderone.com/instory-product-discovery-solution/"),
    APP("App", "https://insiderone.com/channels/app/"),
    SMS_RCS("SMS & RCS", "https://insiderone.com/channels/sms/"),
    ZERO_MIGRATION("Join the $0 Migration Movement™", "https://insiderone.com/the-zero-dollar-migration-movement/"),
    INSIDER_ONE_DIFFERENCE("The Insider One Difference", "https://insiderone.com/why-insiderone/"),
    COMPARE_VENDORS("Compare Vendors", "https://insiderone.com/compare-insiderone/"),
    SWITCH_TO_INSIDER("Switch to Insider One", "https://insiderone.com/insiderone-switch/");

    private final String title;
    private final String url;

    public static Map<String, String> toMap() {
        return Arrays.stream(values())
                .collect(Collectors.toMap(PlatformMenuItem::getTitle, PlatformMenuItem::getUrl));
    }
}
