package tdd.commerce.api.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tdd.commerce.Seller;
import tdd.commerce.SellerRepository;
import tdd.commerce.command.CreateSellerCommand;

import static tdd.commerce.UserPropertyValidator.isEmailValid;
import static tdd.commerce.UserPropertyValidator.isPasswordValid;
import static tdd.commerce.UserPropertyValidator.isUsernameValid;

@RestController
public record SellerSignUpController(
    PasswordEncoder passwordEncoder,
    SellerRepository sellerRepository
) {

    @PostMapping("/seller/signUp")
    ResponseEntity<?> signUp(@RequestBody CreateSellerCommand command) {
        if (!isCommandValid(command)) {
            return ResponseEntity.badRequest().build();
        }

        String hashedPassword = passwordEncoder.encode(command.password());

        UUID id = UUID.randomUUID();
        var seller = new Seller();
        seller.setEmail(command.email());
        seller.setId(id);
        seller.setUsername(command.username());
        seller.setHashedPassword(hashedPassword);
        sellerRepository.save(seller);

        return ResponseEntity.noContent().build();
    }

    private static boolean isCommandValid(CreateSellerCommand command) {
        return isEmailValid(command.email())
            && isUsernameValid(command.username())
            && isPasswordValid(command.password());
    }
}
