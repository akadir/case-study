package io.github.akadir.casestudy.delivery.strategy;

import io.github.akadir.casestudy.delivery.Deliverable;
import io.github.akadir.casestudy.delivery.strategy.DeliveryStrategy;
import io.github.akadir.casestudy.delivery.strategy.FedexDelivery;
import io.github.akadir.casestudy.product.model.Category;
import io.github.akadir.casestudy.product.model.Product;
import io.github.akadir.casestudy.shopping.service.ShoppingCartService;
import io.github.akadir.casestudy.shopping.service.impl.ConcreteShoppingCartServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class FedexDeliveryTest {
    private ShoppingCartService shoppingCartService;

    @Before
    public void init() {
        shoppingCartService = new ConcreteShoppingCartServiceImpl();
    }

    @Test
    public void whenUPSDeliveryCostCalculatedWithNonEmptyProductCartThenExpectCorrectCost() {
        Category category = new Category("Accessories");
        Product product = new Product("bag", 50, category);

        shoppingCartService.addProduct(product);

        double costPerDelivery = 3;
        double costPerProduct = 1;

        DeliveryStrategy deliveryStrategy = new FedexDelivery(costPerProduct, costPerDelivery);

        Map<Product, Integer> productsBackup = new HashMap<>(shoppingCartService.getProducts());

        Deliverable deliverable = shoppingCartService.getShoppingCart();

        assertThat(deliveryStrategy.calculateDeliveryCost(deliverable))
                .as("Delivery cost should be correct")
                .isEqualTo(costPerDelivery + costPerProduct);

        assertThat(shoppingCartService.getProducts())
                .as("Shopping cart should not be modified in delivery cost calculation")
                .isEqualTo(productsBackup);

        Product anotherProduct = new Product("back bag", 100, category);

        shoppingCartService.addProduct(anotherProduct, 2);

        productsBackup = new HashMap<>(shoppingCartService.getProducts());

        assertThat(deliveryStrategy.calculateDeliveryCost(deliverable))
                .as("Delivery cost should be correct")
                .isEqualTo((costPerDelivery * deliverable.getDeliveryCount()) + (costPerProduct * deliverable.getProductCount()));

        assertThat(shoppingCartService.getProducts())
                .as("Shopping cart should not be modified in delivery cost calculation")
                .isEqualTo(productsBackup);
    }

    @Test
    public void whenUPSDeliveryCostCalculatedWithEmptyCartThenExpectZeroCost() {
        DeliveryStrategy deliveryStrategy = new FedexDelivery(1, 3);

        Map<Product, Integer> productsBackup = new HashMap<>(shoppingCartService.getProducts());

        assertThat(deliveryStrategy.calculateDeliveryCost(shoppingCartService.getShoppingCart()))
                .as("Delivery cost should be zero for empty cart")
                .isZero();

        assertThat(shoppingCartService.getProducts())
                .as("Shopping cart should not be modified in delivery cost calculation")
                .isEqualTo(productsBackup);
    }
}
