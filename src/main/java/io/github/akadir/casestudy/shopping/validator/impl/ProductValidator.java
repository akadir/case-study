package io.github.akadir.casestudy.shopping.validator.impl;

import io.github.akadir.casestudy.product.model.Product;
import io.github.akadir.casestudy.shopping.validator.CartEventValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductValidator extends CartEventValidator {
    private final Logger log = LoggerFactory.getLogger(ProductValidator.class);

    @Override
    public boolean check(Product product, int amount) {
        if (product != null) {
            return checkNext(product, amount);
        }

        log.debug("Product is null, cannot be added or removed into/from cart");
        return false;
    }
}
