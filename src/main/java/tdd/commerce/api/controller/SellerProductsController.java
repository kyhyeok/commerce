package tdd.commerce.api.controller;

import java.net.URI;
import java.security.Principal;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tdd.commerce.SellerRepository;
import tdd.commerce.command.RegisterProductCommand;

@RestController
public record SellerProductsController(
    SellerRepository repository
) {

    @PostMapping("/seller/products")
    ResponseEntity<?> registerProduct(
        Principal user,
        @RequestBody RegisterProductCommand command
    ) {
        UUID id = UUID.fromString(user.getName());

        if (repository.findById(id).isEmpty()) {
            return ResponseEntity.status(403).build();
        } else if (!isValidUri(command.imageUri())) {
            return ResponseEntity.badRequest().build();
        }

        URI location = URI.create("/seller/products/" + UUID.randomUUID());

        return ResponseEntity.created(location).build();
    }

    private boolean isValidUri(String value) {
        try {
            URI uri = URI.create(value);
            return uri.getHost() != null;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }
}
