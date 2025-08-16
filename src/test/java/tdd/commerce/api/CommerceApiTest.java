package tdd.commerce.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.test.context.SpringBootTest;
import tdd.commerce.CommerceApplication;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = { CommerceApplication.class, TestFixtureConfiguration.class },
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public @interface CommerceApiTest {
}
