package tdd.commerce;

import java.math.BigDecimal;
import java.util.function.Predicate;

import tdd.commerce.command.RegisterProductCommand;
import tdd.commerce.view.SellerProductView;
import org.assertj.core.api.ThrowingConsumer;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductAssertions {

    public static ThrowingConsumer<SellerProductView> isDerivedFrom(
        RegisterProductCommand command
    ) {
        return product -> {
            System.out.println("Expected: " + command.priceAmount());
            System.out.println("Actual: " + product.priceAmount());
            System.out.println("Expected scale: " + command.priceAmount().scale());
            System.out.println("Actual scale: " + product.priceAmount().scale());
            System.out.println("CompareTo result: " + product.priceAmount().compareTo(command.priceAmount()));

            assertThat(product.name()).isEqualTo(command.name());
            assertThat(product.imageUri()).isEqualTo(command.imageUri());
            assertThat(product.description()).isEqualTo(command.description());
            assertThat(product.priceAmount())
                .matches(equals(command.priceAmount()));
            assertThat(product.stockQuantity())
                .isEqualTo(command.stockQuantity());
        };
    }

    private static Predicate<? super BigDecimal> equals(BigDecimal expected) {
        return actual -> actual.compareTo(expected) == 0;
    }
}
