package tdd.commerce;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import tdd.commerce.command.RegisterProductCommand;

public class RegisterProductCommandGenerator {
    public static RegisterProductCommand generateRegisterProductCommand() {
        return new RegisterProductCommand(
            generateProductName(),
            generateProductImageUri(),
            generateProductDescription(),
            generateProductPriceAmount(),
            generateProductStockQuantity()
        );
    }

    public static RegisterProductCommand generateRegisterProductCommandWithImageUri(String imageUri) {
        return new RegisterProductCommand(
            generateProductName(),
            imageUri,
            generateProductDescription(),
            generateProductPriceAmount(),
            generateProductStockQuantity()
        );
    }

    private static String generateProductName() {
        return "name" + UUID.randomUUID();
    }

    private static String generateProductImageUri() {
        return "https://test.com/images/" + UUID.randomUUID();
    }

    private static String generateProductDescription() {
        return "description" + UUID.randomUUID();
    }

    private static BigDecimal generateProductPriceAmount() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return BigDecimal.valueOf(random.nextInt(10_000, 100_000));
    }

    private static int generateProductStockQuantity() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return random.nextInt(10, 100);

    }
}
