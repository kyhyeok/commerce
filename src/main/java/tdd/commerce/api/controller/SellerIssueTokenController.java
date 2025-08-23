package tdd.commerce.api.controller;

import io.jsonwebtoken.Jwts;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tdd.commerce.Seller;
import tdd.commerce.SellerRepository;
import tdd.commerce.api.JwtKeyHolder;
import tdd.commerce.query.IssueSellerToken;
import tdd.commerce.result.AccessTokenCarrier;

@RestController
public record SellerIssueTokenController(
    SellerRepository sellerRepository,
    PasswordEncoder passwordEncoder,
    JwtKeyHolder jwtKeyHolder
) {

    @PostMapping("/seller/issueToken")
    ResponseEntity<?> issueToken(@RequestBody IssueSellerToken query) {
        return sellerRepository
            .findByEmail(query.email())
            .filter(seller -> passwordEncoder.matches(query.password(), seller.getHashedPassword()))
            .map(this::composeToken)
            .map(AccessTokenCarrier::new)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    private String composeToken(Seller seller) {
        return Jwts
            .builder()
            .setSubject(seller.getId().toString())
            .claim("scp", "seller")
            .signWith(jwtKeyHolder.key())
            .compact();
    }
}
