package io.github.akadir.casestudy.shopping.model;

import io.github.akadir.casestudy.product.model.Category;
import io.github.akadir.casestudy.product.model.Product;
import io.github.akadir.casestudy.shopping.service.model.ShoppingCart;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ShoppingCartTest {

    @Test
    public void whenShoppingCartCreatedThenExpectProductsInitializedAndDiscountsEmpty() {
        ShoppingCart shoppingCart = new ShoppingCart();

        assertThat(shoppingCart.getProducts())
                .as("Shopping cart's products should be empty")
                .isEmpty();

        assertThat(shoppingCart.getAppliedCampaign())
                .as("Applied campaign should be null")
                .isNull();

        assertThat(shoppingCart.getAppliedCoupon())
                .as("Applied coupon should be null")
                .isNull();

        assertThat(shoppingCart.getCartPrice())
                .as("Cart price should be zero")
                .isZero();

        assertThat(shoppingCart.getDiscount())
                .as("Cart discount should be zero")
                .isZero();
    }


    @Test
    public void whenProductAddedToShoppingCartThenExpectProductsIsNotEmptyAndPriceIncreased() {
        ShoppingCart shoppingCart = new ShoppingCart();

        Product foo = new Product("foo", 10, new Category("bar"));
        int amount = 1;

        shoppingCart.getProducts().put(foo, amount);

        assertThat(shoppingCart.getProducts())
                .as("Shopping cart's products size should be " + amount)
                .hasSize(amount);

        assertThat(shoppingCart.getAppliedCampaign())
                .as("Applied campaign should be null")
                .isNull();

        assertThat(shoppingCart.getAppliedCoupon())
                .as("Applied coupon should be null")
                .isNull();

        assertThat(shoppingCart.getCartPrice())
                .as("Cart price should be same as product price times amount: " + foo.getPrice() * amount)
                .isEqualTo(foo.getPrice() * amount);

        assertThat(shoppingCart.getDiscount())
                .as("Cart discount should be zero")
                .isZero();
    }
}
