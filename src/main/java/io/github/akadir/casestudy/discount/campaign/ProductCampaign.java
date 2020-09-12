package io.github.akadir.casestudy.discount.campaign;

import io.github.akadir.casestudy.discount.campaign.base.Campaign;
import io.github.akadir.casestudy.discount.enumeration.DiscountType;
import io.github.akadir.casestudy.product.model.Product;

import java.util.Map;

public class ProductCampaign extends Campaign {
    private final Product targetProduct;

    public ProductCampaign(Product targetProduct, DiscountType discountType, double discountAmount) {
        super(discountType, discountAmount);
        this.targetProduct = targetProduct;
    }

    @Override
    public boolean isCampaignApplicable(Product product, int amount) {
        return product.equals(targetProduct) && discountType.isApplicable(product.getPrice(), discountAmount);
    }

    @Override
    public boolean isCampaignApplicable(Map<Product, Integer> shoppingCart) {
        return shoppingCart.entrySet().stream().anyMatch(e -> isCampaignApplicable(e.getKey(), e.getValue()));
    }
}
