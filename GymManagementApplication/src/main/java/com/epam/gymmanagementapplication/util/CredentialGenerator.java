package com.epam.gymmanagementapplication.util;

import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class CredentialGenerator {
    private CredentialGenerator() {
    }
    private static final String CLASS_NAME = CredentialGenerator.class.getName();
    private static final String GENERATE_RANDOM_PASSWORD = "generateRandomPassword";
    private static final String GET_RANDOM_CHAR = "getRandomChar";
    private static final String GENERATE_USERNAME = "generateUsername";
    private static final String GET_RANDOM_NUMBER = "getRandomNumber";


    private static final String UPPER_CASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER_CASE_CHARS = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*";

    private static final SecureRandom random = new SecureRandom();

    public static String generateRandomPassword(int length) {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(), GENERATE_RANDOM_PASSWORD, CLASS_NAME, ""+length);
        String allChars = UPPER_CASE_CHARS + LOWER_CASE_CHARS + DIGITS + SPECIAL_CHARS;
        List<Character> passwordChars = new ArrayList<>();
        passwordChars.add(getRandomChar(UPPER_CASE_CHARS));
        passwordChars.add(getRandomChar(LOWER_CASE_CHARS));
        passwordChars.add(getRandomChar(DIGITS));
        passwordChars.add(getRandomChar(SPECIAL_CHARS));
        for (int i = 4; i < length; i++) {
            int randomIndex = random.nextInt(allChars.length());
            passwordChars.add(allChars.charAt(randomIndex));
        }
        Collections.shuffle(passwordChars);
        StringBuilder password = new StringBuilder(passwordChars.size());
        for (Character c : passwordChars) {
            password.append(c);
        }
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(), GENERATE_RANDOM_PASSWORD, CLASS_NAME);
        return password.toString();
    }
    private static char getRandomChar(String charSet) {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(), GET_RANDOM_CHAR, CLASS_NAME, charSet);
        int randomIndex = random.nextInt(charSet.length());
        char ch = charSet.charAt(randomIndex);
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(), GET_RANDOM_CHAR, CLASS_NAME);
        return ch;
    }
    public static String generateUsername(String firstName, String lastName) {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(), GENERATE_USERNAME, CLASS_NAME, firstName+" , "+ lastName);
        String initials = firstName.toLowerCase() + lastName.toLowerCase();
        String randomNumber = getRandomNumber();
        String username = initials + randomNumber;
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(), GENERATE_USERNAME, CLASS_NAME);
        return username;
    }
    private static String getRandomNumber() {
        log.info(StringConstants.ENTERED_SERVICE_MESSAGE.getValue(), GET_RANDOM_NUMBER, CLASS_NAME);
        String randomPassword = String.valueOf(random.nextInt((99999 - 10000) + 1) + 10000);
        log.info(StringConstants.EXITING_SERVICE_MESSAGE.getValue(), GET_RANDOM_NUMBER, CLASS_NAME);
        return randomPassword;
    }
}
