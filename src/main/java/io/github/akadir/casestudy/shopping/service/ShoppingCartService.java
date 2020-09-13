package io.github.akadir.casestudy.shopping.service;

import io.github.akadir.casestudy.discount.campaign.base.Campaign;
import io.github.akadir.casestudy.discount.coupon.base.Coupon;
import io.github.akadir.casestudy.product.model.Product;
import io.github.akadir.casestudy.shopping.model.ShoppingCart;

import java.util.Map;

public interface ShoppingCartService {

    void addProduct(Product product);

    void addProduct(Product product, int amount);

    void removeProduct(Product product);

    void removeProduct(Product product, int amount);

    boolean applyCoupon(Coupon coupon);

    boolean applyCampaign(Campaign campaign);

    double getCartPrice();

    double getDiscounts();

    ShoppingCart getShoppingCart();

    Map<Product, Integer> getProducts();
}
