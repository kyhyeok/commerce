package tdd.commerce.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tdd.commerce.Seller;
import tdd.commerce.SellerRepository;
import tdd.commerce.view.SellerMeView;

import java.security.Principal;
import java.util.UUID;

@RestController
public record SellerMeController(
    SellerRepository repository
) {
    @GetMapping("/seller/me")
    SellerMeView me(Principal user) {
        UUID id = UUID.fromString(user.getName());
        Seller seller = repository.findById(id).orElseThrow();
        return new SellerMeView(id, seller.getEmail(), seller.getUsername());
    }
}
