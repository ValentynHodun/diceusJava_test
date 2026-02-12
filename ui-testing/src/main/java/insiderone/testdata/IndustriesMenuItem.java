package insiderone.testdata;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum IndustriesMenuItem {

    RETAIL_ECOMMERCE("Retail & Ecommerce", "https://insiderone.com/industries/retail/"),
    FINANCIAL_SERVICES("Financial Services", "https://insiderone.com/industries/finance/"),
    TRAVEL_HOSPITALITY("Travel & Hospitality", "https://insiderone.com/industries/travel/"),
    BEAUTY_COSMETICS("Beauty & Cosmetics", "https://insiderone.com/beauty-cosmetics/"),
    TELECOMMUNICATIONS("Telecommunications", "https://insiderone.com/industries/telecom/"),
    AUTOMOTIVE("Automotive", "https://insiderone.com/industries/automotive/"),
    SUCCESS_STORY("Success Story", "https://insiderone.com/case-studies/mac-cosmetics/");

    private final String title;
    private final String url;

    public static Map<String, String> toMap() {
        return Arrays.stream(values())
                .collect(Collectors.toMap(IndustriesMenuItem::getTitle, IndustriesMenuItem::getUrl));
    }
}
