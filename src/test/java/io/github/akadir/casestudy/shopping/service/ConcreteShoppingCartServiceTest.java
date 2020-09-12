package io.github.akadir.casestudy.shopping.service;

import io.github.akadir.casestudy.product.Category;
import io.github.akadir.casestudy.product.Product;
import io.github.akadir.casestudy.shopping.service.impl.ConcreteShoppingCartServiceImpl;
import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ConcreteShoppingCartServiceTest {

    @Test
    public void whenClassCreatedThenExpectAllFieldsIsInitialisedAndNoException() {
        ShoppingCartService shoppingCart = new ConcreteShoppingCartServiceImpl();

        assertThat(shoppingCart)
                .as("Shopping cart should not be null")
                .isNotNull();

        assertThat(shoppingCart.getProducts())
                .as("Shopping cart's product list should not be null")
                .isNotNull();

        assertThat(shoppingCart.getProducts())
                .as("Shopping cart's product list should be empty")
                .isEmpty();
    }

    @Test
    public void whenProductAddedCartThenExpectCartHasExactAmountOfThatProduct() {
        ShoppingCartService shoppingCart = new ConcreteShoppingCartServiceImpl();

        Category category = new Category("foo");
        Product product = new Product("bar", 1, category);

        shoppingCart.addProduct(product);

        assertThat(shoppingCart.getProducts())
                .as("Shopping cart products size should be 1")
                .hasSize(1);

        assertThat(shoppingCart.getProducts())
                .as("Shopping cart product amount should be 1")
                .containsEntry(product, 1);

        shoppingCart.addProduct(product, 10);

        assertThat(shoppingCart.getProducts())
                .as("Shopping cart products size should be 1")
                .hasSize(1);

        assertThat(shoppingCart.getProducts())
                .as("Shopping cart product amount should be 11")
                .containsEntry(product, 11);
    }

    @Test
    public void whenNotPositiveAmountOfProductAddedToCartThenExpectProductCartNotChanged() {
        ShoppingCartService shoppingCart = new ConcreteShoppingCartServiceImpl();

        Category category = new Category("foo");
        Product product = new Product("bar", 1, category);

        shoppingCart.addProduct(product, 11);
        shoppingCart.addProduct(product, -1);
        shoppingCart.addProduct(product, -4);

        assertThat(shoppingCart.getProducts())
                .as("Shopping cart products size should be 1")
                .hasSize(1);

        assertThat(shoppingCart.getProducts())
                .as("Shopping cart product amount should be 11")
                .containsEntry(product, 11);
    }

    @Test
    public void whenProductRemovedFromCartThenExpectAmountOfProductDecreasedOrRemoved() {
        ShoppingCartService shoppingCart = new ConcreteShoppingCartServiceImpl();

        Category category = new Category("foo");
        Product product = new Product("bar", 1, category);

        shoppingCart.addProduct(product, 11);

        shoppingCart.removeProduct(product, 1);

        assertThat(shoppingCart.getProducts())
                .as("Shopping cart products size should be 1")
                .hasSize(1);

        assertThat(shoppingCart.getProducts())
                .as("Shopping cart product amount should be 10")
                .containsEntry(product, 10);

        shoppingCart.removeProduct(product, 10);

        assertThat(shoppingCart.getProducts())
                .as("Shopping cart products should be empty")
                .isEmpty();

        assertThat(shoppingCart.getProducts().get(product))
                .as("Shopping cart product amount should return null")
                .isNull();
    }

    @Test
    public void whenNonPositiveAmountOfProductRemovedFromCartThenExpectAmountOfProductNotChanged() {
        ShoppingCartService shoppingCart = new ConcreteShoppingCartServiceImpl();

        Category category = new Category("foo");
        Product product = new Product("bar", 1, category);

        shoppingCart.addProduct(product, 11);

        shoppingCart.removeProduct(product, -1);
        shoppingCart.removeProduct(product, -5);

        assertThat(shoppingCart.getProducts())
                .as("Shopping cart products size should be 1")
                .hasSize(1);

        assertThat(shoppingCart.getProducts())
                .as("Shopping cart product amount should be 11")
                .containsEntry(product, 11);
    }

    @Test
    public void whenChangingProductListFromUsingGetterExpectExceptionThrown() {
        ShoppingCartService shoppingCart = new ConcreteShoppingCartServiceImpl();
        Product foo = new Product("foo", 1, new Category("bar"));

        shoppingCart.addProduct(foo, 3);

        Map<Product, Integer> productsInShoppingCart = shoppingCart.getProducts();

        assertThatThrownBy(() -> productsInShoppingCart.put(foo, 1))
                .as("Adding new product through getter should not be permitted.")
                .isInstanceOf(UnsupportedOperationException.class);

        assertThatThrownBy(() -> productsInShoppingCart.remove(foo, 1))
                .as("Removing product through getter should not be permitted.")
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
