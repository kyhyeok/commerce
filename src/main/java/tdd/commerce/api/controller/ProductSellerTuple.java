package tdd.commerce.api.controller;

import tdd.commerce.Product;
import tdd.commerce.Seller;
import tdd.commerce.view.ProductView;
import tdd.commerce.view.SellerView;

record ProductSellerTuple(
    Product product, Seller seller
) {

    static ProductView toView(ProductSellerTuple tuple) {
        return new ProductView(
            tuple.product().getId(),
            new SellerView(
                tuple.seller().getId(),
                tuple.seller().getUsername()
            ),
            tuple.product().getName(),
            tuple.product().getImageUri(),
            tuple.product().getDescription(),
            tuple.product().getPriceAmount(),
            tuple.product().getStockQuantity()
        );
    }
}
