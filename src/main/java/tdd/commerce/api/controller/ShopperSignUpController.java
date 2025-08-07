package tdd.commerce.api.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tdd.commerce.Shopper;
import tdd.commerce.ShopperRepository;
import tdd.commerce.command.CreateShopperCommand;

import static tdd.commerce.UserPropertyValidator.isEmailValid;
import static tdd.commerce.UserPropertyValidator.isPasswordValid;
import static tdd.commerce.UserPropertyValidator.isUsernameValid;

@RestController
public record ShopperSignUpController(
    PasswordEncoder passwordEncoder,
    ShopperRepository repository
) {
    @PostMapping("/shopper/signUp")
    ResponseEntity<?> signUp(@RequestBody CreateShopperCommand command) {
        if (!isCommandValid(command)) {
            return ResponseEntity.badRequest().build();
        }

        var shopper = new Shopper();
        shopper.setEmail(command.email());
        shopper.setUsername(command.username());
        shopper.setHashedPassword(passwordEncoder.encode(command.password()));
        repository.save(shopper);

        return ResponseEntity.noContent().build();
    }

    private static boolean isCommandValid(CreateShopperCommand command) {
        return isEmailValid(command.email())
            && isUsernameValid(command.username())
            && isPasswordValid(command.password());
    }
}
