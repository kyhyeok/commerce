package test.tdd.commerce.api.shopper.issuetoken;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import tdd.commerce.command.CreateShopperCommand;
import tdd.commerce.query.IssueShopperToken;
import tdd.commerce.result.AccessTokenCarrier;
import test.tdd.commerce.api.CommerceApiTest;

import java.util.Objects;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.*;
import static test.tdd.commerce.EmailGenerator.generateEmail;
import static test.tdd.commerce.JwtAssertions.conformsToJwtFormat;
import static test.tdd.commerce.PasswordGenerator.generatePassword;
import static test.tdd.commerce.UsernameGenerator.generateUsername;

@CommerceApiTest
@DisplayName("POST /shopper/issueToken")
public class POST_specs {

    @Test
    void 올바르게_요청하면_200_OK_상태코드와_접근_토큰을_반환한다(
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        String email = generateEmail();
        String password = generatePassword();

        client.postForEntity("/shopper/signUp",
            new CreateShopperCommand(email, generateUsername(), password),
            Void.class
        );
        // Act

        ResponseEntity<AccessTokenCarrier> response = client.postForEntity(
            "/shopper/issueToken",
            new IssueShopperToken(email, password),
            AccessTokenCarrier.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().accessToken()).isNotNull();
    }

    @Test
    void 접근_토큰은_JWT_형식을_따른다(
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        String email = generateEmail();
        String password = generatePassword();

        client.postForEntity("/shopper/signUp",
            new CreateShopperCommand(email, generateUsername(), password),
            Void.class
        );
        // Act

        ResponseEntity<AccessTokenCarrier> response = client.postForEntity(
            "/shopper/issueToken",
            new IssueShopperToken(email, password),
            AccessTokenCarrier.class
        );

        // Assert
        String actual = requireNonNull(response.getBody()).accessToken();
        assertThat(actual).satisfies(conformsToJwtFormat());
    }

    @Test
    void 존재하지_않는_이메일_주소가_사용되면_400_Bad_Request_상태코드를_반환한다(
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        String email = generateEmail();
        String password = generatePassword();

        // Act
        ResponseEntity<AccessTokenCarrier> response = client.postForEntity(
            "/shopper/issueToken",
            new IssueShopperToken(email, password),
            AccessTokenCarrier.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void 잘못된_비밀번호가_사용되면_400_Bad_Request_상태코드를_반환한다(
        @Autowired TestRestTemplate client
    ){
        // Arrange
        String email = generateEmail();
        String password = generatePassword();
        String wrongPassword = generatePassword();

        client.postForEntity("/shopper/signUp",
            new CreateShopperCommand(email, generateUsername(), password),
            Void.class
        );
        // Act

        ResponseEntity<AccessTokenCarrier> response = client.postForEntity(
            "/shopper/issueToken",
            new IssueShopperToken(email, wrongPassword),
            AccessTokenCarrier.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }
}
