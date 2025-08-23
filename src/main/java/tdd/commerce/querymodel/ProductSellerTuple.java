package tdd.commerce.querymodel;

import tdd.commerce.Product;
import tdd.commerce.Seller;
import tdd.commerce.view.ProductView;
import tdd.commerce.view.SellerView;

record ProductSellerTuple(
    Product product, Seller seller
) {

    ProductView toView() {
        return new ProductView(product().getId(),
            new SellerView(seller().getId(), seller().getUsername()),
            product().getName(),
            product().getImageUri(),
            product().getDescription(),
            product().getPriceAmount(),
            product().getStockQuantity()
        );
    }
}
