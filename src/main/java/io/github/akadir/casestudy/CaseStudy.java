package io.github.akadir.casestudy;

import io.github.akadir.casestudy.discount.campaign.CategoryCampaign;
import io.github.akadir.casestudy.discount.campaign.base.Campaign;
import io.github.akadir.casestudy.discount.coupon.ConcreteCoupon;
import io.github.akadir.casestudy.discount.coupon.base.Coupon;
import io.github.akadir.casestudy.discount.enumeration.DiscountType;
import io.github.akadir.casestudy.product.model.Category;
import io.github.akadir.casestudy.product.model.Product;
import io.github.akadir.casestudy.shopping.service.ShoppingCartService;
import io.github.akadir.casestudy.shopping.service.impl.ConcreteShoppingCartServiceImpl;

public class CaseStudy {

    public static void main(String[] args) {
        Category food = new Category("food");

        Product apple = new Product("Apple", 100.0, food);
        Product almond = new Product("Almonds", 150.0, food);

        ShoppingCartService shoppingCartService = new ConcreteShoppingCartServiceImpl();

        shoppingCartService.addProduct(apple);
        shoppingCartService.addProduct(almond);

        Campaign campaign = new CategoryCampaign(food, DiscountType.AMOUNT, 10);
        Coupon coupon = new ConcreteCoupon(50.0, DiscountType.RATE, 10);

        shoppingCartService.applyCampaign(campaign);
        shoppingCartService.applyCoupon(coupon);
    }
}
