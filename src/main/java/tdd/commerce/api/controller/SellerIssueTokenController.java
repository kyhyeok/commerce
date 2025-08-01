package tdd.commerce.api.controller;

import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tdd.commerce.Seller;
import tdd.commerce.SellerRepository;
import tdd.commerce.query.IssueSellerToken;
import tdd.commerce.result.AccessTokenCarrier;

import java.util.Optional;

@RestController
public record SellerIssueTokenController(
    SellerRepository sellerRepository,
    PasswordEncoder passwordEncoder,
    @Value("${security.jwt.secret}") String jwtSecret
) {

    @PostMapping("/seller/issueToken")
    ResponseEntity<?> issueToken(@RequestBody IssueSellerToken query) {
        return sellerRepository
            .findByEmail(query.email())
            .filter(seller -> passwordEncoder.matches(query.password(), seller.getHashedPassword()))
            .map(seller -> composeToken())
            .map(AccessTokenCarrier::new)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    private String composeToken() {
        return Jwts
            .builder()
            .signWith(new SecretKeySpec(jwtSecret.getBytes(), "HmacSHA256"))
            .compact();
    }
}
