package test.tdd.commerce.api;

import org.springframework.boot.test.context.SpringBootTest;
import tdd.commerce.CommerceApplication;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = CommerceApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public @interface CommerceApiTest {
}
