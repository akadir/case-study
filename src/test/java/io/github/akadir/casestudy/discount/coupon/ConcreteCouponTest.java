package io.github.akadir.casestudy.discount.coupon;

import io.github.akadir.casestudy.discount.coupon.base.Coupon;
import io.github.akadir.casestudy.discount.enumeration.DiscountType;
import io.github.akadir.casestudy.product.model.Category;
import io.github.akadir.casestudy.product.model.Product;
import io.github.akadir.casestudy.shopping.service.ShoppingCartService;
import io.github.akadir.casestudy.shopping.service.impl.ConcreteShoppingCartServiceImpl;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConcreteCouponTest {

    @Test
    public void whenCouponMinAmountIsLessThanCartAmountThenExpectCorrectDiscount() {
        Coupon coupon = new ConcreteCoupon(15.0, DiscountType.RATE, 10);

        ShoppingCartService shoppingCartService = new ConcreteShoppingCartServiceImpl();

        Category electronic = new Category("electronic");
        Product watch = new Product("watch", 100, electronic);

        shoppingCartService.addProduct(watch);

        assertThat(coupon.isCouponApplicable(shoppingCartService.getProducts()))
                .isTrue();

        assertThat(coupon.calculateDiscount(shoppingCartService.getProducts()))
                .isEqualTo(10);

        coupon = new ConcreteCoupon(15.0, DiscountType.AMOUNT, 20);


        assertThat(coupon.isCouponApplicable(shoppingCartService.getProducts()))
                .isTrue();

        assertThat(coupon.calculateDiscount(shoppingCartService.getProducts()))
                .isEqualTo(20);

        coupon = new ConcreteCoupon(100.0, DiscountType.AMOUNT, 100);

        assertThat(coupon.isCouponApplicable(shoppingCartService.getProducts()))
                .isTrue();

        assertThat(coupon.calculateDiscount(shoppingCartService.getProducts()))
                .isEqualTo(100);

    }

    @Test
    public void whenCouponMinAmountIsNotLessThenCartAmountThenExpectCorrectDiscount() {
        Coupon coupon = new ConcreteCoupon(150.0, DiscountType.RATE, 10);

        ShoppingCartService shoppingCartService = new ConcreteShoppingCartServiceImpl();

        Category electronic = new Category("electronic");
        Product watch = new Product("watch", 100, electronic);

        assertThat(coupon.isCouponApplicable(shoppingCartService.getProducts()))
                .isFalse();

        assertThat(coupon.calculateDiscount(shoppingCartService.getProducts()))
                .isZero();

        coupon = new ConcreteCoupon(150.0, DiscountType.AMOUNT, 10);


        assertThat(coupon.isCouponApplicable(shoppingCartService.getProducts()))
                .isFalse();

        assertThat(coupon.calculateDiscount(shoppingCartService.getProducts()))
                .isZero();
    }

    @Test
    public void whenShoppingCartIsEmptyThenExpectCouponNotApplicableAndDiscountIsZero() {
        Coupon coupon = new ConcreteCoupon(150.0, DiscountType.RATE, 10);

        ShoppingCartService shoppingCartService = new ConcreteShoppingCartServiceImpl();

        assertThat(coupon.isCouponApplicable(shoppingCartService.getProducts()))
                .isFalse();

        assertThat(coupon.calculateDiscount(shoppingCartService.getProducts()))
                .isZero();

        coupon = new ConcreteCoupon(150.0, DiscountType.AMOUNT, 10);


        assertThat(coupon.isCouponApplicable(shoppingCartService.getProducts()))
                .isFalse();

        assertThat(coupon.calculateDiscount(shoppingCartService.getProducts()))
                .isZero();
    }
}
