package io.github.akadir.casestudy.shopping.service.impl;

import io.github.akadir.casestudy.discount.campaign.base.Campaign;
import io.github.akadir.casestudy.discount.coupon.base.Coupon;
import io.github.akadir.casestudy.product.model.Product;
import io.github.akadir.casestudy.shopping.model.ShoppingCart;
import io.github.akadir.casestudy.shopping.service.ShoppingCartService;
import io.github.akadir.casestudy.shopping.validator.CartEventValidator;
import io.github.akadir.casestudy.shopping.validator.impl.AmountValidator;
import io.github.akadir.casestudy.shopping.validator.impl.ProductValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;

public class ConcreteShoppingCartServiceImpl implements ShoppingCartService {
    private final Logger log = LoggerFactory.getLogger(ConcreteShoppingCartServiceImpl.class);

    private final ShoppingCart shoppingCart;
    private final CartEventValidator validator;

    public ConcreteShoppingCartServiceImpl() {
        this.shoppingCart = new ShoppingCart();
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
            int alreadyAdded = shoppingCart.getProducts().getOrDefault(product, 0);
            shoppingCart.getProducts().put(product, alreadyAdded + amount);
            log.info("{} added to cart. New amount: {}", product.getTitle(), alreadyAdded + amount);
        } else {
            log.warn("{} with amount: {} could not be added to chart", product.getTitle(), amount);
        }
    }

    @Override
    public void removeProduct(Product product) {
        removeProduct(product, shoppingCart.getProducts().get(product));
    }

    @Override
    public void removeProduct(Product product, int amount) {
        if (validator.check(product, amount)) {
            int alreadyAdded = shoppingCart.getProducts().get(product);
            if (alreadyAdded <= amount) {
                shoppingCart.getProducts().remove(product);
                log.info("{} removed from cart", product.getTitle());
            } else {
                shoppingCart.getProducts().put(product, alreadyAdded - amount);
                log.info("{} {} removed from cart. new amount is: {}", amount, product.getTitle(), alreadyAdded - amount);
            }

            checkDiscounts();
        } else {
            log.warn("{} with amount: {} could not be removed from chart", product.getTitle(), amount);
        }
    }

    private void checkDiscounts() {
        double newDiscount = 0;

        Campaign appliedCampaign = shoppingCart.getAppliedCampaign();

        if (appliedCampaign != null) {
            double campaignDiscount = appliedCampaign.calculateDiscount(shoppingCart.getProducts());

            if (campaignDiscount == 0) {
                shoppingCart.setAppliedCampaign(null);
                log.info("Campaign removed from cart");
            }

            newDiscount += campaignDiscount;
        }

        Coupon appliedCoupon = shoppingCart.getAppliedCoupon();

        if (appliedCoupon != null) {
            double couponDiscount = appliedCoupon.calculateDiscount(shoppingCart.getProducts());

            if (couponDiscount == 0) {
                shoppingCart.setAppliedCoupon(null);
                log.info("Coupon removed from cart");
            }

            newDiscount += couponDiscount;
        }

        shoppingCart.setDiscount(newDiscount);
    }

    @Override
    public boolean applyCoupon(Coupon coupon) {
        Coupon appliedCoupon = shoppingCart.getAppliedCoupon();

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

            if (cartPrice < couponDiscount + shoppingCart.getDiscount()) {
                log.warn("Discounts exceeded cart price. Add more products into your cart to use this coupon");
                return false;
            }

            shoppingCart.setDiscount(shoppingCart.getDiscount() + couponDiscount);
            shoppingCart.setAppliedCoupon(coupon);
            return true;
        }

        return false;
    }

    @Override
    public boolean applyCampaign(Campaign campaign) {
        Campaign appliedCampaign = shoppingCart.getAppliedCampaign();

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

            if (cartPrice < campaignDiscount + shoppingCart.getDiscount()) {
                log.warn("Discounts exceeded cart price. Add more products into your cart to use this campaign");
                return false;
            }


            shoppingCart.setDiscount(shoppingCart.getDiscount() + campaignDiscount);
            shoppingCart.setAppliedCampaign(campaign);

            return true;
        }

        return false;
    }

    @Override
    public double getCartPrice() {
        return shoppingCart.getCartPrice();
    }

    @Override
    public double getDiscounts() {
        return shoppingCart.getDiscount();
    }

    @Override
    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public Map<Product, Integer> getProducts() {
        return Collections.unmodifiableMap(shoppingCart.getProducts());
    }
}
