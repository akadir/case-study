package io.github.akadir.casestudy.shopping.model;

import io.github.akadir.casestudy.delivery.Deliverable;
import io.github.akadir.casestudy.discount.campaign.base.Campaign;
import io.github.akadir.casestudy.discount.coupon.base.Coupon;
import io.github.akadir.casestudy.product.model.Product;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCart implements Deliverable {
    private final Map<Product, Integer> products;
    private Coupon appliedCoupon;
    private Campaign appliedCampaign;
    private double discount;

    public ShoppingCart() {
        this.products = new HashMap<>();
    }

    public Map<Product, Integer> getProducts() {
        return products;
    }

    public Coupon getAppliedCoupon() {
        return appliedCoupon;
    }

    public void setAppliedCoupon(Coupon appliedCoupon) {
        this.appliedCoupon = appliedCoupon;
    }

    public Campaign getAppliedCampaign() {
        return appliedCampaign;
    }

    public void setAppliedCampaign(Campaign appliedCampaign) {
        this.appliedCampaign = appliedCampaign;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getCartPrice() {
        return products.entrySet().stream()
                .mapToDouble(e -> e.getKey().getPrice() * e.getValue()).sum();
    }

    public int getDeliveryCount() {
        return products.size();
    }

    public int getProductCount() {
        return products.values().stream().mapToInt(a -> a).sum();
    }
}
