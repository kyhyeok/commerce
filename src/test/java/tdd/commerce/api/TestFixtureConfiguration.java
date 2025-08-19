package tdd.commerce.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import tdd.commerce.ProductRepository;

public class TestFixtureConfiguration {
    @Bean
    @Scope("prototype")
    TestFixture testFixture(
        Environment environment,
        ProductRepository productRepository
    ) {
        return TestFixture.create(environment, productRepository);
    }
}
