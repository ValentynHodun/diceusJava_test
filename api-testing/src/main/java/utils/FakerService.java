package utils;

import com.github.javafaker.Faker;

import java.util.Locale;

public class FakerService {

    private FakerService() {

    }

    public synchronized static Faker get() {
        return new Faker();
    }

    public static Faker get(Locale locale) {
        return new Faker(locale);
    }
}
