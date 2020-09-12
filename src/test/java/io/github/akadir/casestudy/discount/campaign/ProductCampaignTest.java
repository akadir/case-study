package io.github.akadir.casestudy.discount.campaign;

import io.github.akadir.casestudy.discount.campaign.base.Campaign;
import io.github.akadir.casestudy.discount.enumeration.DiscountType;
import io.github.akadir.casestudy.product.model.Category;
import io.github.akadir.casestudy.product.model.Product;
import io.github.akadir.casestudy.shopping.service.ShoppingCartService;
import io.github.akadir.casestudy.shopping.service.impl.ConcreteShoppingCartServiceImpl;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductCampaignTest {

    private Product targetProduct;

    @Before
    public void init() {
        Category category = new Category("Food");
        targetProduct = new Product("apple", 10, category);
    }

    @Test
    public void whenProductCampaignAppliedToCorrectProductThenExpectCorrectDiscountCalculation() {
        Campaign campaign = new ProductCampaign(targetProduct, DiscountType.RATE, 10);

        ShoppingCartService shoppingCartService = new ConcreteShoppingCartServiceImpl();
        shoppingCartService.addProduct(targetProduct);

        assertThat(campaign.isCampaignApplicable(targetProduct, 1))
                .as("Product should be applicable to product campaign that targets this product")
                .isTrue();

        assertThat(campaign.isCampaignApplicable(shoppingCartService.getProducts()))
                .as("ShoppingCart that contains targeted product should be applicable to product campaign")
                .isTrue();

        assertThat(campaign.calculateDiscount(shoppingCartService.getProducts()))
                .as("Campaign discount should be correct")
                .isEqualTo(1);

        campaign = new ProductCampaign(targetProduct, DiscountType.AMOUNT, 2);

        assertThat(campaign.isCampaignApplicable(targetProduct, 1))
                .as("Product should be applicable to product campaign that targets this product")
                .isTrue();

        assertThat(campaign.isCampaignApplicable(shoppingCartService.getProducts()))
                .as("ShoppingCart that contains targeted product should be applicable to product campaign")
                .isTrue();

        assertThat(campaign.calculateDiscount(shoppingCartService.getProducts()))
                .as("Campaign discount should be correct")
                .isEqualTo(2);
    }

    @Test
    public void whenShoppingCartIsEmptyThenCampaignShouldReturnZeroDiscount() {
        Campaign campaign = new ProductCampaign(targetProduct, DiscountType.RATE, 120);

        ShoppingCartService shoppingCartService = new ConcreteShoppingCartServiceImpl();

        assertThat(campaign.isCampaignApplicable(shoppingCartService.getProducts()))
                .as("Empty ShoppingCart should not be applicable to product campaign")
                .isFalse();

        assertThat(campaign.calculateDiscount(shoppingCartService.getProducts()))
                .as("Empty ShoppingCarts campaign discount should be zero")
                .isZero();
    }

    @Test
    public void whenProductCampaignAppliedToProductWithLowerPriceThanCampaignThenExpectZeroDiscountGain() {
        Campaign campaign = new ProductCampaign(targetProduct, DiscountType.RATE, 120);

        ShoppingCartService shoppingCartService = new ConcreteShoppingCartServiceImpl();
        shoppingCartService.addProduct(targetProduct);

        assertThat(campaign.isCampaignApplicable(targetProduct, 1))
                .as("Product with cheaper price should not be applicable to product campaign that targets the product")
                .isFalse();

        assertThat(campaign.isCampaignApplicable(shoppingCartService.getProducts()))
                .as("ShoppingCart that contains targeted product with cheaper price should not be applicable to product campaign")
                .isFalse();

        assertThat(campaign.calculateDiscount(shoppingCartService.getProducts()))
                .as("Product should be applicable to product campaign that targets this product")
                .isZero();

        campaign = new ProductCampaign(targetProduct, DiscountType.AMOUNT, 12);

        assertThat(campaign.isCampaignApplicable(targetProduct, 1))
                .as("Product should not be applicable to product campaign that targets the product but discount amount is larger")
                .isFalse();

        assertThat(campaign.isCampaignApplicable(shoppingCartService.getProducts()))
                .as("ShoppingCart that contains targeted product with cheaper price should not be applicable to product campaign")
                .isFalse();

        assertThat(campaign.calculateDiscount(shoppingCartService.getProducts()))
                .as("Product with cheaper price should not be applicable to product campaign that targets this product")
                .isZero();
    }

    @Test
    public void whenProductIsDifferentFromProductCampaignsTargetedProductThenExpectNotApplicableAndZeroDiscount() {
        Campaign campaign = new ProductCampaign(targetProduct, DiscountType.RATE, 120);

        ShoppingCartService shoppingCartService = new ConcreteShoppingCartServiceImpl();

        Product product = new Product("Another product", 10, new Category("Foo"));

        shoppingCartService.addProduct(product);

        assertThat(campaign.isCampaignApplicable(targetProduct, 1))
                .as("Product should not be applicable to product campaign that targets the another product")
                .isFalse();

        assertThat(campaign.isCampaignApplicable(shoppingCartService.getProducts()))
                .as("ShoppingCart that contains not targeted product should not be applicable to product campaign")
                .isFalse();

        assertThat(campaign.calculateDiscount(shoppingCartService.getProducts()))
                .as("Product should not be applicable to product campaign that targets another product")
                .isZero();
    }
}
