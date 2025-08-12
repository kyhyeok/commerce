package tdd.commerce.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tdd.commerce.Shopper;
import tdd.commerce.ShopperRepository;
import tdd.commerce.view.ShopperMeView;

import java.security.Principal;
import java.util.UUID;

@RestController
public record ShopperMeController(
    ShopperRepository repository
) {
    @GetMapping("/shopper/me")
    ShopperMeView me(Principal user) {
        UUID id = UUID.fromString(user.getName());
        Shopper shopper = repository.findById(id).orElseThrow();
        return new ShopperMeView(id, shopper.getEmail(), shopper.getUsername());
    }
}
