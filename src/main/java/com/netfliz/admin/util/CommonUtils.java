package com.netfliz.admin.util;

import org.apache.logging.log4j.util.Strings;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Component
public class CommonUtils {
    public static String generateUsername(String email) {
        if (Strings.isBlank(email)) {
            return null;
        }
        return email.split("@")[0];
    }

    public static String generatePassword() {
        PasswordGenerator generator = new PasswordGenerator();

        CharacterRule lowerCaseRule = new CharacterRule(EnglishCharacterData.LowerCase, 4);
        CharacterRule digitRule = new CharacterRule(EnglishCharacterData.Digit, 3);
        CharacterRule specialRule = new CharacterRule(EnglishCharacterData.Special, 3);

        return generator.generatePassword(10,
                Arrays.asList(lowerCaseRule, digitRule, specialRule));
    }

    private static final DateTimeFormatter DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");

    public static String formatDate(Instant instant) {
        if (instant == null) {
            return null;
        }
        return LocalDateTime.ofInstant(instant, ZoneId.of("Asia/Ho_Chi_Minh"))
                          .format(DATE_FORMATTER);
    }

    public static String formatDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.format(DATE_FORMATTER);
    }

    public static LocalDateTime parseDate(String dateString) {
        if (Strings.isBlank(dateString)) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateString, DATE_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }

    public static LocalDateTime instantToLocalDateTime(Instant instant) {
        if (instant == null) {
            return null;
        }
        return LocalDateTime.ofInstant(instant, ZoneId.of("Asia/Ho_Chi_Minh"));
    }

    public static Instant localDateTimeToInstant(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant();
    }
}
