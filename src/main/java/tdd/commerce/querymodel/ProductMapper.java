package tdd.commerce.querymodel;

import tdd.commerce.Product;
import tdd.commerce.view.SellerProductView;

public class ProductMapper {
    public static SellerProductView convertToView(Product product) {
        return new SellerProductView(
            product.getId(),
            product.getName(),
            product.getImageUri(),
            product.getDescription(),
            product.getPriceAmount(),
            product.getStockQuantity(),
            product.getRegisteredTimeUtc()
        );
    }
}
