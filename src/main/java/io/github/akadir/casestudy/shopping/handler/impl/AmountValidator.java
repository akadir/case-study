package io.github.akadir.casestudy.shopping.handler.impl;

import io.github.akadir.casestudy.product.Product;
import io.github.akadir.casestudy.shopping.handler.CartEventValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AmountValidator extends CartEventValidator {
    private final Logger log = LoggerFactory.getLogger(AmountValidator.class);

    @Override
    public boolean check(Product product, int amount) {
        if (amount > 0) {
            return checkNext(product, amount);
        }

        log.debug("Product {} with amount {} could not be passed amount validator.", product, amount);
        return false;
    }
}
