package tdd.commerce.api.controller;

import io.jsonwebtoken.Jwts;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tdd.commerce.ShopperRepository;
import tdd.commerce.api.JwtKeyHolder;
import tdd.commerce.query.IssueSellerToken;
import tdd.commerce.result.AccessTokenCarrier;

@RestController
public record ShopperIssueTokenController(
    ShopperRepository repository,
    PasswordEncoder passwordEncoder,
    JwtKeyHolder jwtKeyHolder
) {
    @PostMapping("shopper/issueToken")
    ResponseEntity<?> issueToken(@RequestBody IssueSellerToken query) {
        return repository
            .findByEmail(query.email())
            .filter(shopper -> passwordEncoder.matches(
                query.password(),
                shopper.getHashedPassword()
            ))
            .map(shopper -> composeToken())
            .map(AccessTokenCarrier::new)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    private String composeToken() {
        return Jwts
            .builder()
            .signWith(jwtKeyHolder.key())
            .compact();
    }
}
