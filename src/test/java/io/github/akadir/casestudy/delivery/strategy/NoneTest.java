package io.github.akadir.casestudy.delivery.strategy;

import io.github.akadir.casestudy.product.model.Product;
import io.github.akadir.casestudy.shopping.service.ShoppingCartService;
import io.github.akadir.casestudy.shopping.service.impl.ConcreteShoppingCartServiceImpl;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class NoneTest {

    @Test
    public void whenNoneDeliveryCostCalculatedThenExpectZeroCost() {
        DeliveryStrategy deliveryStrategy = new None();

        ShoppingCartService shoppingCartService = new ConcreteShoppingCartServiceImpl();

        Map<Product, Integer> productsBackup = new HashMap<>(shoppingCartService.getProducts());

        assertThat(deliveryStrategy.calculateDeliveryCost(shoppingCartService.getShoppingCart()))
                .as("Should return zero for shopping cart object")
                .isZero();

        assertThat(deliveryStrategy.calculateDeliveryCost(null))
                .as("Should return zero for null")
                .isZero();

        assertThat(shoppingCartService.getProducts())
                .as("Shopping cart should not be modified in delivery cost calculation")
                .isEqualTo(productsBackup);
    }
}
