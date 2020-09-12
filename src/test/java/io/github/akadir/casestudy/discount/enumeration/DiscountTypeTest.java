package io.github.akadir.casestudy.discount.enumeration;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DiscountTypeTest {

    @Test
    public void whenDiscountTypeRateAndRateIsCorrectThenExpectPriceIsApplicableAndDiscountAmountIsCorrect() {
        assertThat(DiscountType.RATE.isApplicable(10, 10))
                .isTrue();

        assertThat(DiscountType.RATE.isApplicable(Integer.MAX_VALUE, 10))
                .isTrue();

        assertThat(DiscountType.RATE.calculateDiscount(100, 10))
                .isEqualTo(10);

        assertThat(DiscountType.RATE.calculateDiscount(10, 10))
                .isEqualTo(1);

        assertThat(DiscountType.RATE.calculateDiscount(10, 0))
                .isZero();

        assertThat(DiscountType.RATE.calculateDiscount(10, 100))
                .isZero();
    }

    @Test
    public void whenDiscountTypeRateAndRateIsMoreThan100OrLessThanZeroThenExpectPriceIsNotApplicableAndDiscountAmountIsZero() {
        assertThat(DiscountType.RATE.isApplicable(10, 110))
                .isFalse();

        assertThat(DiscountType.RATE.isApplicable(Integer.MAX_VALUE, 101))
                .isFalse();

        assertThat(DiscountType.RATE.isApplicable(Integer.MAX_VALUE, 100))
                .isFalse();

        assertThat(DiscountType.RATE.isApplicable(Integer.MAX_VALUE, 0))
                .isFalse();

        assertThat(DiscountType.RATE.isApplicable(Integer.MAX_VALUE, -40))
                .isFalse();

        assertThat(DiscountType.RATE.calculateDiscount(100, 200))
                .isZero();

        assertThat(DiscountType.RATE.calculateDiscount(10000, 150))
                .isZero();

        assertThat(DiscountType.RATE.calculateDiscount(10, -310))
                .isZero();

        assertThat(DiscountType.RATE.calculateDiscount(10, -100))
                .isZero();
    }

    @Test
    public void whenDiscountTypeIsAmountAndCorrectThenExpectDiscountApplicableAndDiscountCorrect() {
        assertThat(DiscountType.AMOUNT.isApplicable(10, 1))
                .isTrue();

        assertThat(DiscountType.AMOUNT.isApplicable(Integer.MAX_VALUE, 1))
                .isTrue();

        assertThat(DiscountType.AMOUNT.isApplicable(Double.MAX_VALUE, Integer.MAX_VALUE - 1))
                .isTrue();

        assertThat(DiscountType.AMOUNT.calculateDiscount(10, 1))
                .isEqualTo(1);

        assertThat(DiscountType.AMOUNT.calculateDiscount(Double.MAX_VALUE, 1))
                .isEqualTo(1);

        assertThat(DiscountType.AMOUNT.calculateDiscount(Double.MAX_VALUE, Integer.MAX_VALUE - 1))
                .isEqualTo(Integer.MAX_VALUE - 1);
    }

    @Test
    public void whenDiscountTypeIsAmountAndPriceIsLessThanDiscountTypeThenExpectNotApplicableAndZeroDiscount() {
        assertThat(DiscountType.AMOUNT.isApplicable(10, 11))
                .isFalse();

        assertThat(DiscountType.AMOUNT.isApplicable(10, 0))
                .isFalse();

        assertThat(DiscountType.AMOUNT.isApplicable(Integer.MAX_VALUE - 1, Integer.MAX_VALUE))
                .isFalse();

        assertThat(DiscountType.AMOUNT.isApplicable(Integer.MAX_VALUE - 1, Double.MAX_VALUE))
                .isFalse();

        assertThat(DiscountType.AMOUNT.calculateDiscount(10, 11))
                .isZero();

        assertThat(DiscountType.AMOUNT.calculateDiscount(10, 0))
                .isZero();

        assertThat(DiscountType.AMOUNT.calculateDiscount(Integer.MAX_VALUE - 1, Integer.MAX_VALUE))
                .isZero();

        assertThat(DiscountType.AMOUNT.calculateDiscount(Integer.MAX_VALUE - 1, Double.MAX_VALUE))
                .isZero();
    }
}
