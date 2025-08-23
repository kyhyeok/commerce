package tdd.commerce.api.controller;

import jakarta.persistence.EntityManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tdd.commerce.query.GetProductPage;
import tdd.commerce.querymodel.GetProductPageQueryProcessor;
import tdd.commerce.result.PageCarrier;
import tdd.commerce.view.ProductView;

@RestController
public record ShopperProductsController(
    EntityManager entityManager
) {

    @GetMapping("/shopper/products")
    PageCarrier<ProductView> getProducts(@RequestParam(required = false) String continuationToken) {
        var processor = new GetProductPageQueryProcessor(entityManager);
        var query = new GetProductPage(continuationToken);
        return processor.process(query);
    }
}
