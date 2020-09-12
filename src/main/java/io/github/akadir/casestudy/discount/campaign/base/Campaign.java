package io.github.akadir.casestudy.discount.campaign.base;

import io.github.akadir.casestudy.discount.enumeration.DiscountType;
import io.github.akadir.casestudy.product.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.stream.Collectors;

public abstract class Campaign {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    protected final DiscountType discountType;
    protected final double discountAmount;

    protected Campaign(DiscountType discountType, double discountAmount) {
        this.discountType = discountType;
        this.discountAmount = discountAmount;
    }

    public abstract boolean isCampaignApplicable(Product product, int amount);

    public abstract boolean isCampaignApplicable(Map<Product, Integer> shoppingCart);

    public double calculateDiscount(Map<Product, Integer> shoppingCart) {
        Map<Product, Integer> applicableProducts = shoppingCart.entrySet()
                .stream().filter(e -> isCampaignApplicable(e.getKey(), e.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (applicableProducts.isEmpty()) {
            log.warn("There is no applicable product in cart for this campaign: {}", this.getClass().getSimpleName());
            return 0;
        } else {
            double totalDiscount = 0d;
            for (Map.Entry<Product, Integer> entry : applicableProducts.entrySet()) {
                Product product = entry.getKey();
                int amount = entry.getValue();

                double calculatedDiscount = amount * discountType.calculateDiscount(product.getPrice(), this.discountAmount);

                log.info("Discount for {} calculated as {} with campaign {}", product.getTitle(), calculatedDiscount,
                        this.getClass().getSimpleName());

                totalDiscount += calculatedDiscount;
            }

            log.info("Total discount gained from campaign {} is {}", this.getClass().getSimpleName(), totalDiscount);

            return totalDiscount;
        }
    }
}
