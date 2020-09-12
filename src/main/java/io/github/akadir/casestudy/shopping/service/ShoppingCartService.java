package io.github.akadir.casestudy.shopping.service;

import io.github.akadir.casestudy.product.Product;

import java.util.Map;

public interface ShoppingCartService {

    void addProduct(Product product);

    void addProduct(Product product, int amount);

    void removeProduct(Product product);

    void removeProduct(Product product, int amount);

    Map<Product, Integer> getProducts();
}
