package io.github.akadir.casestudy.discount.coupon.base;

import io.github.akadir.casestudy.discount.enumeration.DiscountType;
import io.github.akadir.casestudy.product.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class Coupon {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    protected final double minimumCartAmount;
    protected final DiscountType discountType;
    protected final double discountAmount;

    protected Coupon(double minimumCartAmount, DiscountType discountType, double discountAmount) {
        this.minimumCartAmount = minimumCartAmount;
        this.discountType = discountType;
        this.discountAmount = discountAmount;
    }

    public abstract boolean isCouponApplicable(Map<Product, Integer> shoppingCart);

    public double calculateDiscount(Map<Product, Integer> shoppingCart) {
        if (isCouponApplicable(shoppingCart)) {
            double totalAmount = shoppingCart.entrySet().stream()
                    .mapToDouble(e -> e.getKey().getPrice() * e.getValue()).sum();

            double totalDiscount = discountType.calculateDiscount(totalAmount, discountAmount);
            log.info("Total discount gained from coupon {} is {}", this.getClass().getSimpleName(), totalDiscount);

            return totalDiscount;
        } else {
            log.info("Coupon {} is not applicable to shopping cart", this.getClass().getSimpleName());
            return 0;
        }
    }
}
