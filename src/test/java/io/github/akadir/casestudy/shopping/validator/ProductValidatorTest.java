package io.github.akadir.casestudy.shopping.validator;

import io.github.akadir.casestudy.product.model.Category;
import io.github.akadir.casestudy.product.model.Product;
import io.github.akadir.casestudy.shopping.validator.impl.ProductValidator;
import org.junit.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ProductValidatorTest {

    @Test
    public void whenProductIsNullThenExpectCheckNextNotCalledAndFalse() {
        CartEventValidator productValidator = Mockito.spy(ProductValidator.class);

        assertThat(productValidator.check(null, 0))
                .as("null products should not pass amount validator")
                .isFalse();

        assertThat(productValidator.check(null, Integer.MIN_VALUE))
                .as("null products should not pass amount validator")
                .isFalse();

        assertThat(productValidator.check(null, -5))
                .as("null products should not pass amount validator")
                .isFalse();

        verify(productValidator, Mockito.never()).checkNext(any(Product.class), anyInt());
    }

    @Test
    public void whenProductIsNotNullThenExpectCheckNextCalledAndTrue() {
        CartEventValidator amountValidator = Mockito.spy(ProductValidator.class);
        Product product = new Product("foo", 0, new Category("bar"));
        Product anotherProduct = new Product("apple", 0, new Category("food"));

        assertThat(amountValidator.check(product, 1))
                .as("Positive amounts should pass amount validator")
                .isTrue();

        verify(amountValidator, times(1)).checkNext(any(Product.class), anyInt());

        assertThat(amountValidator.check(anotherProduct, Integer.MAX_VALUE))
                .as("Positive amounts should pass amount validator")
                .isTrue();

        verify(amountValidator, times(2)).checkNext(any(Product.class), anyInt());
    }

}
