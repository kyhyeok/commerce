package tdd.commerce;

import java.math.BigDecimal;
import java.util.function.Predicate;

import org.assertj.core.api.ThrowingConsumer;
import tdd.commerce.command.RegisterProductCommand;
import tdd.commerce.view.ProductView;
import tdd.commerce.view.SellerProductView;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductAssertions {

    public static ThrowingConsumer<SellerProductView> isDerivedFrom(
        RegisterProductCommand command
    ) {
        return product -> {
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

    public static ThrowingConsumer<? super ProductView> isViewDerivedFrom(RegisterProductCommand command) {
        return product -> {
            assertThat(product.name()).isEqualTo(command.name());
            assertThat(product.imageUri()).isEqualTo(command.imageUri());
            assertThat(product.description()).isEqualTo(command.description());
            assertThat(product.priceAmount())
                .matches(equals(command.priceAmount()));
            assertThat(product.stockQuantity())
                .isEqualTo(command.stockQuantity());
        };
    }
}
