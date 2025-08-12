package test.tdd.commerce.api;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.web.client.RestTemplate;
import tdd.commerce.command.CreateShopperCommand;
import tdd.commerce.query.IssueSellerToken;
import tdd.commerce.result.AccessTokenCarrier;
import tdd.commerce.view.ShopperMeView;

import static org.springframework.http.RequestEntity.get;
import static test.tdd.commerce.EmailGenerator.generateEmail;
import static test.tdd.commerce.PasswordGenerator.generatePassword;
import static test.tdd.commerce.UsernameGenerator.generateUsername;

public record TestFixture(
    TestRestTemplate client
) {

    public void createShopper(String email, String username, String password) {
        var command = new CreateShopperCommand(email, username, password);
        client.postForEntity("/shopper/signUp", command, Void.class);
    }

    public String issueShopperToken(String email, String password) {
        AccessTokenCarrier carrier = client.postForObject(
                "/shopper/issueToken",
                new IssueSellerToken(email, password),
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
        RestTemplate template = client.getRestTemplate();
        template.getInterceptors().add((request, body, execution) -> {
            if (!request.getHeaders().containsKey("Authorization")) {
                request.getHeaders().add("Authorization", "Bearer " + token);
            }

            return execution.execute(request, body);
        });
    }
}
