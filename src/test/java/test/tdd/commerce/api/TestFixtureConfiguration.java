package test.tdd.commerce.api;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;

public class TestFixtureConfiguration {
    @Bean
    TestFixture testFixture(
        BeanFactory beanFactory
    ) {
        return new TestFixture(beanFactory.getBean(TestRestTemplate.class));
    }
}
