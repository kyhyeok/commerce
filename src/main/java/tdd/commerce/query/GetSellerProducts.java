package tdd.commerce.query;

import java.util.UUID;

public record GetSellerProducts(
    UUID sellerId
) {
}
