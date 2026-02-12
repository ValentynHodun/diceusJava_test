package insiderone.testdata;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum ResourcesMenuItem {
    INTERACTIVE_PLATFORM_TOUR("Interactive Platform Tour", "https://insiderone.com/product-demo-hub/%20"),
    PRODUCT_DEMO_HUB("Product Demo Hub", "https://insiderone.com/product-demo-hub/%20"),
    WHATSAPP_EXPLORER("WhatsApp Explorer", "https://insiderone.com/whatsapp-templates/"),
    SMS_TEMPLATE_LIBRARY("SMS Template Library", "https://insiderone.com/sms-templates/"),
    CDP_EXPLORER("CDP Explorer", "https://insiderone.com/cdp-use-cases-explorer/"),
    CASE_STUDIES("Case Studies", "https://insiderone.com/case-studies"),
    BLOG("Blog", "https://insiderone.com/blog"),
    EBOOKS_GUIDES("E-Books & Guides", "https://insiderone.com/ebook"),
    GLOSSARY("Glossary", "https://insiderone.com/glossary");

    private final String title;
    private final String url;

    public static Map<String, String> toMap() {
        return Arrays.stream(values())
                .collect(Collectors.toMap(ResourcesMenuItem::getTitle, ResourcesMenuItem::getUrl));
    }
}
