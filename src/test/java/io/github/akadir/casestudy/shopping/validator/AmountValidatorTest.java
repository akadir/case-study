package io.github.akadir.casestudy.shopping.validator;

import io.github.akadir.casestudy.product.model.Category;
import io.github.akadir.casestudy.product.model.Product;
import io.github.akadir.casestudy.shopping.validator.impl.AmountValidator;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class AmountValidatorTest {

    @Test
    public void whenAmountIsNonPositiveThenExpectCheckNextNotCalledAndFalse() {
        CartEventValidator amountValidator = Mockito.spy(AmountValidator.class);

        assertThat(amountValidator.check(null, 0))
                .as("Non positive amounts should not pass amount validator")
                .isFalse();

        assertThat(amountValidator.check(new Product("foo", 0, new Category("bar")), 0))
                .as("Non positive amounts should not pass amount validator")
                .isFalse();

        assertThat(amountValidator.check(null, -5))
                .as("Non positive amounts should not pass amount validator")
                .isFalse();

        int random = ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE, 1);

        assertThat(amountValidator.check(new Product("foo", 0, new Category("bar")), random))
                .as("Non positive amounts should not pass amount validator")
                .isFalse();

        assertThat(amountValidator.check(null, Integer.MIN_VALUE))
                .as("Non positive amounts should not pass amount validator")
                .isFalse();

        verify(amountValidator, Mockito.never()).checkNext(any(Product.class), anyInt());
    }

    @Test
    public void whenAmountIsGreaterThanZeroThenExpectCheckNextCalledAndTrue() {
        CartEventValidator amountValidator = Mockito.spy(AmountValidator.class);

        assertThat(amountValidator.check(null, 1))
                .as("Positive amounts should pass amount validator")
                .isTrue();

        verify(amountValidator, times(1)).checkNext(any(Product.class), anyInt());

        assertThat(amountValidator.check(null, 1000))
                .as("Positive amounts should pass amount validator")
                .isTrue();

        assertThat(amountValidator.check(null, Integer.MAX_VALUE))
                .as("Positive amounts should pass amount validator")
                .isTrue();

        assertThat(amountValidator.check(new Product("foo", 0, new Category("bar")), 4))
                .as("Positive amounts should pass amount validator")
                .isTrue();

        int random = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);

        assertThat(amountValidator.check(new Product("foo", 0, new Category("bar")), random))
                .as("Positive amounts should pass amount validator")
                .isTrue();

        verify(amountValidator, times(5)).checkNext(any(Product.class), anyInt());
    }
}
