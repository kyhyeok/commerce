package tdd.commerce.api;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.web.client.LocalHostUriTemplateHandler;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import tdd.commerce.ProductRepository;
import tdd.commerce.command.CreateSellerCommand;
import tdd.commerce.command.CreateShopperCommand;
import tdd.commerce.command.RegisterProductCommand;
import tdd.commerce.query.IssueSellerToken;
import tdd.commerce.query.IssueShopperToken;
import tdd.commerce.result.AccessTokenCarrier;
import tdd.commerce.result.PageCarrier;
import tdd.commerce.view.ProductView;
import tdd.commerce.view.SellerMeView;

import static java.util.Objects.requireNonNull;
import static org.springframework.http.RequestEntity.*;
import static tdd.commerce.EmailGenerator.generateEmail;
import static tdd.commerce.PasswordGenerator.generatePassword;
import static tdd.commerce.RegisterProductCommandGenerator.generateRegisterProductCommand;
import static tdd.commerce.UsernameGenerator.generateUsername;

public record TestFixture(
    TestRestTemplate client,
    ProductRepository productRepository
) {

    public static TestFixture create(Environment environment, ProductRepository productRepository) {
        var client = new TestRestTemplate();
        var uriTemplateHandler = new LocalHostUriTemplateHandler(environment);
        client.setUriTemplateHandler(uriTemplateHandler);
        return new TestFixture(client, productRepository);
    }

    public void createShopper(String email, String username, String password) {
        var command = new CreateShopperCommand(email, username, password);
        client.postForEntity("/shopper/signUp", command, Void.class);
    }

    public String issueShopperToken(String email, String password) {
        AccessTokenCarrier carrier = client.postForObject(
            "/shopper/issueToken",
            new IssueShopperToken(email, password),
            AccessTokenCarrier.class
        );
        return carrier.accessToken();
    }

    public String createShopperThenIssueToken() {
        String email = generateEmail();
        String password = generatePassword();
        createShopper(email, generateUsername(), password);
        return issueShopperToken(email, password);
    }

    public void setShopperAsDefaultUser(String email, String password) {
        String token = issueShopperToken(email, password);
        setDefaultAuthorization("Bearer " + token);
    }

    private void setDefaultAuthorization(String authorization) {
        RestTemplate template = client.getRestTemplate();
        template.getInterceptors().addFirst((request, body, execution) -> {
            if (!request.getHeaders().containsKey("Authorization")) {
                request.getHeaders().add("Authorization", authorization);
            }

            return execution.execute(request, body);
        });
    }

    public void createSellerThenSetAsDefaultUser() {
        String email = generateEmail();
        String password = generatePassword();
        createSeller(email, generateUsername(), password);
        setSellerAsDefaultUser(email, password);
    }

    private void createSeller(String email, String username, String password) {
        var command = new CreateSellerCommand(email, username, password);
        client.postForEntity("/seller/signUp", command, Void.class);
    }

    private void setSellerAsDefaultUser(String email, String password) {
        String token = issueSellerToken(email, password);
        setDefaultAuthorization("Bearer " + token);
    }

    private String issueSellerToken(String email, String password) {
        AccessTokenCarrier carrier = client.postForObject(
            "/seller/issueToken",
            new IssueSellerToken(email, password),
            AccessTokenCarrier.class
        );
        return carrier.accessToken();
    }

    public void createShopperThenSetAsDefaultUser() {
        String email = generateEmail();
        String password = generatePassword();
        createShopper(email, generateUsername(), password);
        setShopperAsDefaultUser(email, password);
    }

    public UUID registerProduct() {
        return registerProduct(generateRegisterProductCommand());
    }

    @NotNull
    public UUID registerProduct(RegisterProductCommand command) {
        ResponseEntity<Void> response = client.postForEntity(
            "/seller/products",
            command,
            Void.class
        );

        URI location = response.getHeaders().getLocation();
        String path = requireNonNull(location).getPath();
        String id = path.substring("/seller/products/".length());
        return UUID.fromString(id);
    }

    public List<UUID> registerProducts() {
        return List.of(
            registerProduct(),
            registerProduct(),
            registerProduct()
        );
    }

    public void deleteAllProducts() {
        productRepository.deleteAll();
    }

    public List<UUID> registerProducts(int count) {
        List<UUID> ids = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ids.add(registerProduct());
        }
        return ids;
    }

    public SellerMeView getSeller() {
        return client.getForObject("/seller/me", SellerMeView.class);
    }

    public String consumeProductPage() {
        ResponseEntity<PageCarrier<ProductView>> response = client.exchange(
            get("/shopper/products").build(),
            new ParameterizedTypeReference<>() { }
        );

        return requireNonNull(response.getBody()).continuationToken();
    }

    public String consumeTwoProductPages() {
        String token = consumeProductPage();
        ResponseEntity<PageCarrier<ProductView>> response = client.exchange(
            get("/shopper/products?continuationToken=%s".formatted(token)).build(),
            new ParameterizedTypeReference<>() { }
        );

        return requireNonNull(response.getBody()).continuationToken();
    }
}
