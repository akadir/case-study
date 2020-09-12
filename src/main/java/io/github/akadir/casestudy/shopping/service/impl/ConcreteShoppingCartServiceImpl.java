package io.github.akadir.casestudy.shopping.service.impl;

import io.github.akadir.casestudy.discount.campaign.base.Campaign;
import io.github.akadir.casestudy.discount.coupon.base.Coupon;
import io.github.akadir.casestudy.product.model.Product;
import io.github.akadir.casestudy.shopping.service.ShoppingCartService;
import io.github.akadir.casestudy.shopping.validator.CartEventValidator;
import io.github.akadir.casestudy.shopping.validator.impl.AmountValidator;
import io.github.akadir.casestudy.shopping.validator.impl.ProductValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ConcreteShoppingCartServiceImpl implements ShoppingCartService {
    private final Logger log = LoggerFactory.getLogger(ConcreteShoppingCartServiceImpl.class);

    private final Map<Product, Integer> products;
    private final CartEventValidator validator;
    private Coupon appliedCoupon;
    private Campaign appliedCampaign;
    private double discount;

    public ConcreteShoppingCartServiceImpl() {
        this.products = new HashMap<>();
        this.validator = new ProductValidator();
        validator.linkWith(new AmountValidator());
    }

    @Override
    public void addProduct(Product product) {
        this.addProduct(product, 1);
    }

    @Override
    public void addProduct(Product product, int amount) {
        if (validator.check(product, amount)) {
            int alreadyAdded = products.getOrDefault(product, 0);
            products.put(product, alreadyAdded + amount);
            log.info("{} added to cart. New amount: {}", product.getTitle(), alreadyAdded + amount);
        } else {
            log.warn("{} with amount: {} could not be added to chart", product.getTitle(), amount);
        }
    }

    @Override
    public void removeProduct(Product product) {
        removeProduct(product, products.get(product));
    }

    @Override
    public void removeProduct(Product product, int amount) {
        if (validator.check(product, amount)) {
            int alreadyAdded = products.get(product);
            if (alreadyAdded <= amount) {
                products.remove(product);
                log.info("{} removed from cart", product.getTitle());
            } else {
                products.put(product, alreadyAdded - amount);
                log.info("{} {} removed from cart. new amount is: {}", amount, product.getTitle(), alreadyAdded - amount);
            }

            checkDiscounts();
        } else {
            log.warn("{} with amount: {} could not be removed from chart", product.getTitle(), amount);
        }
    }

    private void checkDiscounts() {
        double newDiscount = 0;

        if (appliedCampaign != null) {
            double campaignDiscount = appliedCampaign.calculateDiscount(products);

            if (campaignDiscount == 0) {
                this.appliedCampaign = null;
                log.info("Campaign removed from cart");
            }

            newDiscount += campaignDiscount;
        }

        if (appliedCoupon != null) {
            double couponDiscount = appliedCoupon.calculateDiscount(products);

            if (couponDiscount == 0) {
                this.appliedCoupon = null;
                log.info("Coupon removed from cart");
            }

            newDiscount += couponDiscount;
        }

        this.discount = newDiscount;
    }

    @Override
    public boolean applyCoupon(Coupon coupon) {
        if (appliedCoupon != null) {
            log.warn("You have already applied another coupon");
            return false;
        }

        if (coupon != null) {
            double couponDiscount = coupon.calculateDiscount(this.getProducts());

            if (couponDiscount == 0) {
                log.warn("Coupon has no effect on total price of cart");
                return false;
            }

            double cartPrice = getCartPrice();

            if (cartPrice < couponDiscount + this.discount) {
                log.warn("Discounts exceeded cart price. Add more products into your cart to use this coupon");
                return false;
            }

            this.discount += couponDiscount;
            this.appliedCoupon = coupon;
            return true;
        }

        return false;
    }

    @Override
    public boolean applyCampaign(Campaign campaign) {
        if (appliedCampaign != null) {
            log.warn("You have already applied another campaign");
            return false;
        }

        if (campaign != null) {
            double campaignDiscount = campaign.calculateDiscount(this.getProducts());

            if (campaignDiscount == 0) {
                log.warn("Campaign has no effect on total price of cart");
                return false;
            }

            double cartPrice = getCartPrice();

            if (cartPrice < campaignDiscount + this.discount) {
                log.warn("Discounts exceeded cart price. Add more products into your cart to use this campaign");
                return false;
            }

            this.discount += campaignDiscount;
            this.appliedCampaign = campaign;

            return true;
        }

        return false;
    }

    @Override
    public double getCartPrice() {
        return products.entrySet().stream()
                .mapToDouble(e -> e.getKey().getPrice() * e.getValue()).sum();
    }

    @Override
    public double getDiscounts() {
        return discount;
    }

    public Map<Product, Integer> getProducts() {
        return Collections.unmodifiableMap(products);
    }
}
