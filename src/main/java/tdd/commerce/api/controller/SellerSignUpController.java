package tdd.commerce.api.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tdd.commerce.Seller;
import tdd.commerce.SellerRepository;
import tdd.commerce.command.CreateSellerCommand;

@RestController
public record SellerSignUpController(SellerRepository sellerRepository) {
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    public static final String USERNAME_REGEX = "^[a-zA-Z0-9_-]{3,}$";

    @PostMapping("/seller/signUp")
    ResponseEntity<?> signUp(@RequestBody CreateSellerCommand command) {
        if (!isCommandValid(command)) {
            return ResponseEntity.badRequest().build();
        }

        var seller = new Seller();
        seller.setEmail(command.email());
        seller.setUsername(command.username());

        try {
            sellerRepository.save(seller);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.noContent().build();
    }

    private static boolean isCommandValid(CreateSellerCommand command) {
        return isEmailValid(command.email())
            && isUsernameValid(command.username())
            && isPasswordValid(command.password());
    }

    private static boolean isEmailValid(String email) {
        return email != null && email.matches(EMAIL_REGEX);
    }

    private static boolean isUsernameValid(String username) {
        return username != null && username.matches(USERNAME_REGEX);
    }

    private static boolean isPasswordValid(String password) {
        return password != null && password.length() >= 8;
    }
}
