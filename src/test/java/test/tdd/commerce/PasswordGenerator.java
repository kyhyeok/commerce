package test.tdd.commerce;

import java.util.UUID;

public class PasswordGenerator {

    public static String generatePassword() {
        return "password" + UUID.randomUUID();
    }
}
