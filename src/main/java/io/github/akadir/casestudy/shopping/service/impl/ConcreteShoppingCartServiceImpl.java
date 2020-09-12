package io.github.akadir.casestudy.shopping.service.impl;

import io.github.akadir.casestudy.product.Product;
import io.github.akadir.casestudy.shopping.handler.CartEventValidator;
import io.github.akadir.casestudy.shopping.handler.impl.AmountValidator;
import io.github.akadir.casestudy.shopping.handler.impl.ProductValidator;
import io.github.akadir.casestudy.shopping.service.ShoppingCartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ConcreteShoppingCartServiceImpl implements ShoppingCartService {
    private final Logger log = LoggerFactory.getLogger(ConcreteShoppingCartServiceImpl.class);

    private final Map<Product, Integer> products;
    private final CartEventValidator validator;

    public ConcreteShoppingCartServiceImpl() {
        this.products = new HashMap<>();
        this.validator = new ProductValidator();
        validator.linkWith(new AmountValidator());
    }

    @Override
    public void addProduct(Product product) {
        this.addProduct(product, 1);
    }

    @Override
    public void addProduct(Product product, int amount) {
        if (validator.check(product, amount)) {
            int alreadyAdded = products.getOrDefault(product, 0);
            products.put(product, alreadyAdded + amount);
            log.info("{} added to chart. New amount: {}", product.getTitle(), alreadyAdded + amount);
        } else {
            log.warn("{} with amount: {} could not be added to chart", product.getTitle(), amount);
        }
    }

    @Override
    public void removeProduct(Product product) {
        removeProduct(product, products.get(product));
    }

    @Override
    public void removeProduct(Product product, int amount) {
        if (validator.check(product, amount)) {
            int alreadyAdded = products.get(product);
            if (alreadyAdded <= amount) {
                products.remove(product);
                log.info("{} removed from cart", product.getTitle());
            } else {
                products.put(product, alreadyAdded - amount);
                log.info("{} {} removed from cart. new amount is: {}", amount, product.getTitle(), alreadyAdded - amount);
            }
        } else {
            log.warn("{} with amount: {} could not be removed from chart", product.getTitle(), amount);
        }
    }

    public Map<Product, Integer> getProducts() {
        return Collections.unmodifiableMap(products);
    }
}
