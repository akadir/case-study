package io.github.akadir.casestudy.discount.coupon;

import io.github.akadir.casestudy.discount.coupon.base.Coupon;
import io.github.akadir.casestudy.discount.enumeration.DiscountType;
import io.github.akadir.casestudy.product.model.Product;

import java.util.Map;

public class ConcreteCoupon extends Coupon {
    protected ConcreteCoupon(Double minimumCartAmount, DiscountType discountType, double discountAmount) {
        super(minimumCartAmount, discountType, discountAmount);
    }

    @Override
    public boolean isCouponApplicable(Map<Product, Integer> shoppingCart) {
        double totalAmount = shoppingCart.entrySet().stream()
                .mapToDouble(e -> e.getKey().getPrice() * e.getValue())
                .sum();

        return totalAmount >= minimumCartAmount;
    }
}
