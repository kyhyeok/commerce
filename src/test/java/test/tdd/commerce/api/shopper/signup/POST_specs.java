package test.tdd.commerce.api.shopper.signup;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import tdd.commerce.Seller;
import tdd.commerce.SellerRepository;
import tdd.commerce.ShopperRepository;
import tdd.commerce.command.CreateSellerCommand;
import tdd.commerce.command.CreateShopperCommand;
import test.tdd.commerce.api.CommerceApiTest;

import static org.assertj.core.api.Assertions.assertThat;
import static test.tdd.commerce.EmailGenerator.generateEmail;
import static test.tdd.commerce.PasswordGenerator.generatePassword;
import static test.tdd.commerce.UsernameGenerator.generateUsername;

@CommerceApiTest
@DisplayName("/shopper/signUp")
public class POST_specs {

    @Test
    void 올바르게_요청하면_204_No_Content_상태코드를_반환한다(
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        CreateShopperCommand command = new CreateShopperCommand(
            generateEmail(),
            generateUsername(),
            generatePassword()
        );
        // Act
        ResponseEntity<Void> response = client.postForEntity(
            "/shopper/signUp",
            command,
            Void.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(204);
    }

    @Test
    void email_속성이_지정되지_않으면_400_Bad_Request_상태코드를_반환한다(
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        CreateShopperCommand command = new CreateShopperCommand(
            null,
            generateUsername(),
            generatePassword()
        );
        // Act
        ResponseEntity<Void> response = client.postForEntity(
            "/shopper/signUp",
            command,
            Void.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "invalid-email",
        "invalid-email@",
        "invalid-email@test",
        "invalid-email@test.",
        "invalid-email@.com.",
    })
    void email_속성이_올바른_형식을_따르지_않으면_400_Bad_Request_상태코드를_반환한다(
        String email,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        CreateShopperCommand command = new CreateShopperCommand(
            email,
            generateUsername(),
            generatePassword()
        );
        // Act
        ResponseEntity<Void> response = client.postForEntity(
            "/shopper/signUp",
            command,
            Void.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void username_속성이_지정되지_않으면_400_Bad_Request_상태코드를_반환한다(
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        CreateShopperCommand command = new CreateShopperCommand(
            generateEmail(),
            null,
            generatePassword()
        );
        // Act
        ResponseEntity<Void> response = client.postForEntity(
            "/shopper/signUp",
            command,
            Void.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "",
        "sh",
        "shopper ",
        "shopper.",
        "shopper!",
        "shopper@",
    })
    void username_속성이_올바른_형식을_따르지_않으면_400_Bad_Request_상태코드를_반환한다(
        String username,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        CreateShopperCommand command = new CreateShopperCommand(
            generateEmail(),
            username,
            generatePassword()
        );
        // Act
        ResponseEntity<Void> response = client.postForEntity(
            "/shopper/signUp",
            command,
            Void.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "shopper",
        "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
        "0123456789",
        "shopper_",
        "shopper-"
    })
    void username_속성이_올바른_형식을_따르면_204_No_Content_상태코드를_반환한다(
        String username,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        CreateShopperCommand command = new CreateShopperCommand(
            generateEmail(),
            username,
            generatePassword()
        );
        // Act
        ResponseEntity<Void> response = client.postForEntity(
            "/shopper/signUp",
            command,
            Void.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(204);
    }

    @Test
    void password_속성이_지정되지_않으면_400_Bad_Request_상태코드를_반환한다(
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        var command = new CreateSellerCommand(
            generateEmail(),
            generateUsername(),
            null
        );

        // Act
        ResponseEntity<Void> response = client.postForEntity(
            "/shopper/signUp",
            command,
            Void.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @ParameterizedTest
    @MethodSource("test.tdd.commerce.TestDataSource#invalidPasswords")
    void password_속성이_올바른_형식을_따르지_않으면_400_Bad_Request_상태코드를_반환한다(
        String password,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        var command = new CreateSellerCommand(
            generateEmail(),
            generateUsername(),
            password
        );

        // Act
        ResponseEntity<Void> response = client.postForEntity(
            "/shopper/signUp",
            command,
            Void.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void email_속성에_이미_존재하는_이메일_주소가_지정되면_400_Bad_Request_상태코드를_반환한다(
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        String email = generateEmail();


        client.postForEntity("/shopper/signUp", new CreateShopperCommand(
            email,
            generateUsername(),
            generatePassword()
        ), Void.class);

        // Act
        ResponseEntity<Void> response = client.postForEntity(
            "/shopper/signUp", new CreateShopperCommand(
                email,
                generateUsername(),
                generatePassword()
            ), Void.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void username_속성에_이미_존재하는_이메일_주소가_지정되면_400_Bad_Request_상태코드를_반환한다(
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        String username = "shopper";

        client.postForEntity(
            "/shopper/signUp",
            new CreateSellerCommand(generateEmail(), username, generatePassword()),
            Void.class
        );

        // Act
        ResponseEntity<Void> response = client.postForEntity(
            "/shopper/signUp",
            new CreateSellerCommand(generateEmail(), username, generatePassword()),
            Void.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void 비밀번호를_올바르게_암호화한다(
        @Autowired TestRestTemplate client,
        @Autowired ShopperRepository repository,
        @Autowired PasswordEncoder encoder
    ) {
        // Arrange
        var command = new CreateShopperCommand(
            generateEmail(),
            generateUsername(),
            generatePassword()
        );

        // Act
        client.postForEntity("/shopper/signUp", command, Void.class);

        // Assert
        var shopper = repository
            .findAll()
            .stream()
            .filter(x -> x.getEmail().equals(command.email()))
            .findFirst()
            .orElseThrow();

        String actual = shopper.getHashedPassword();
        assertThat(actual).isNotNull();
        assertThat(encoder.matches(command.password(), actual)).isTrue();
    }

}
