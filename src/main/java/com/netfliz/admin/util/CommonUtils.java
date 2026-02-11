package com.netfliz.admin.util;

import org.apache.logging.log4j.util.Strings;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.stereotype.Component;

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
}
