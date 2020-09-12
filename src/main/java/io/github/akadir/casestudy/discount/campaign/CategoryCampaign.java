package io.github.akadir.casestudy.discount.campaign;

import io.github.akadir.casestudy.discount.campaign.base.Campaign;
import io.github.akadir.casestudy.discount.enumeration.DiscountType;
import io.github.akadir.casestudy.product.model.Category;
import io.github.akadir.casestudy.product.model.Product;

import java.util.Map;

public class CategoryCampaign extends Campaign {
    private final Category targetCategory;

    public CategoryCampaign(Category targetCategory, DiscountType discountType, double discountAmount) {
        super(discountType, discountAmount);
        this.targetCategory = targetCategory;
    }

    @Override
    public boolean isCampaignApplicable(Product product, int amount) {
        Category productCategory = product.getCategory();
        while (productCategory != null) {
            if (targetCategory.equals(productCategory) && discountType.isApplicable(product.getPrice(), discountAmount)) {
                return true;
            }

            productCategory = productCategory.getParentCategory();
        }

        return false;
    }

    @Override
    public boolean isCampaignApplicable(Map<Product, Integer> shoppingCart) {
        return shoppingCart.entrySet().stream().anyMatch(e -> isCampaignApplicable(e.getKey(), e.getValue()));
    }
}
