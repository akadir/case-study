package io.github.akadir.casestudy.shopping.model;

import io.github.akadir.casestudy.delivery.Deliverable;
import io.github.akadir.casestudy.delivery.strategy.DeliveryStrategy;
import io.github.akadir.casestudy.delivery.strategy.None;
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
    private DeliveryStrategy deliveryStrategy;

    public ShoppingCart() {
        this.products = new HashMap<>();
        this.deliveryStrategy = new None();
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

    @Override
    public DeliveryStrategy getDeliveryStrategy() {
        return this.deliveryStrategy;
    }

    @Override
    public void setDeliveryStrategy(DeliveryStrategy deliveryStrategy) {
        this.deliveryStrategy = deliveryStrategy;
    }

    public int getProductCount() {
        return products.values().stream().mapToInt(a -> a).sum();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("\nDelivery count: ");

        sb.append(getDeliveryCount()).append("\nProduct count: ")
                .append(getProductCount()).append("\nPrice: ")
                .append(getCartPrice()).append("\nDiscounts: ").append(discount)
                .append("\nPrice to pay: ").append(getCartPrice() - discount)
                .append("\nDelivery strategy: ").append(deliveryStrategy.getClass().getSimpleName())
                .append("\nDelivery cost: ").append(deliveryStrategy.calculateDeliveryCost(this));

        return sb.toString();
    }
}
