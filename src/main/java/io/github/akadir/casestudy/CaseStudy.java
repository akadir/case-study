package io.github.akadir.casestudy;

import io.github.akadir.casestudy.delivery.strategy.FedexDelivery;
import io.github.akadir.casestudy.delivery.strategy.UPSDelivery;
import io.github.akadir.casestudy.discount.campaign.CategoryCampaign;
import io.github.akadir.casestudy.discount.campaign.base.Campaign;
import io.github.akadir.casestudy.discount.coupon.ConcreteCoupon;
import io.github.akadir.casestudy.discount.coupon.base.Coupon;
import io.github.akadir.casestudy.discount.enumeration.DiscountType;
import io.github.akadir.casestudy.product.model.Category;
import io.github.akadir.casestudy.product.model.Product;
import io.github.akadir.casestudy.shopping.service.ShoppingCartService;
import io.github.akadir.casestudy.shopping.service.impl.ConcreteShoppingCartServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CaseStudy {
    private static final Logger log = LoggerFactory.getLogger(CaseStudy.class);

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

        log.info("Shopping cart details without delivery strategy: {}", shoppingCartService.getShoppingCart());

        shoppingCartService.getShoppingCart().setDeliveryStrategy(new FedexDelivery(1, 5));
        log.info("Shopping cart details with FedexDelivery: {}", shoppingCartService.getShoppingCart());

        log.info("Change delivery strategy to UPSDelivery");

        shoppingCartService.getShoppingCart().setDeliveryStrategy(new UPSDelivery(2, 3));
        log.info("Shopping cart details with UPSDelivery: {}", shoppingCartService.getShoppingCart());

    }
}
