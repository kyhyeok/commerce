package tdd.commerce.view;

import java.util.UUID;

public record ShopperMeView(
    UUID id, String email, String username
) {

}
