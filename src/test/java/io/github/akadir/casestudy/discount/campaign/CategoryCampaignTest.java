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

public class CategoryCampaignTest {


    private Category targetCategory;

    @Before
    public void init() {
        targetCategory = new Category("Food");
    }

    @Test
    public void whenProductCampaignAppliedToCorrectProductThenExpectCorrectDiscountCalculation() {
        Campaign campaign = new CategoryCampaign(targetCategory, DiscountType.RATE, 10);

        Product apple = new Product("apple", 10, targetCategory);

        ShoppingCartService shoppingCartService = new ConcreteShoppingCartServiceImpl();
        shoppingCartService.addProduct(apple);

        assertThat(campaign.isCampaignApplicable(apple, 1))
                .as("Product should be applicable to category campaign that targets this product's category")
                .isTrue();

        assertThat(campaign.isCampaignApplicable(shoppingCartService.getProducts()))
                .as("ShoppingCart that contains targeted category's product should be applicable to category campaign")
                .isTrue();

        assertThat(campaign.calculateDiscount(shoppingCartService.getProducts()))
                .as("Campaign discount should be correct")
                .isEqualTo(1);

        campaign = new ProductCampaign(apple, DiscountType.AMOUNT, 2);

        assertThat(campaign.isCampaignApplicable(apple, 1))
                .as("Product should be applicable to category campaign that targets this product's category")
                .isTrue();

        assertThat(campaign.isCampaignApplicable(shoppingCartService.getProducts()))
                .as("ShoppingCart that contains targeted category's product should be applicable to category campaign")
                .isTrue();

        assertThat(campaign.calculateDiscount(shoppingCartService.getProducts()))
                .as("Campaign discount should be correct")
                .isEqualTo(2);
    }

    @Test
    public void whenShoppingCartIsEmptyThenCampaignShouldReturnZeroDiscount() {
        Campaign campaign = new CategoryCampaign(targetCategory, DiscountType.RATE, 10);

        ShoppingCartService shoppingCartService = new ConcreteShoppingCartServiceImpl();

        assertThat(campaign.isCampaignApplicable(shoppingCartService.getProducts()))
                .as("Empty ShoppingCart should not be applicable to category campaign")
                .isFalse();

        assertThat(campaign.calculateDiscount(shoppingCartService.getProducts()))
                .as("Empty ShoppingCarts campaign discount should be zero")
                .isZero();
    }

    @Test
    public void whenProductCategoryIsNotTargetedByCampaignThenCampaignShouldReturnZeroDiscount() {
        Campaign campaign = new CategoryCampaign(targetCategory, DiscountType.RATE, 10);


        Category electronics = new Category("Electronics");
        Product computer = new Product("computer", 1000, electronics);

        ShoppingCartService shoppingCartService = new ConcreteShoppingCartServiceImpl();

        shoppingCartService.addProduct(computer);

        assertThat(campaign.isCampaignApplicable(shoppingCartService.getProducts()))
                .as("ShoppingCart without targeted Category's product should not be applicable to category campaign")
                .isFalse();

        assertThat(campaign.calculateDiscount(shoppingCartService.getProducts()))
                .as("ShoppingCart without targeted Category's product, campaign discount should be zero")
                .isZero();
    }

    @Test
    public void whenParentCategoryOfProductCategoryHasCampaignThenExpectCorrectDiscount() {
        Campaign campaign = new CategoryCampaign(targetCategory, DiscountType.RATE, 10);

        Category fruits = new Category("Fruits", targetCategory);
        Product apple = new Product("apple", 10, fruits);

        ShoppingCartService shoppingCartService = new ConcreteShoppingCartServiceImpl();
        shoppingCartService.addProduct(apple);

        assertThat(campaign.isCampaignApplicable(apple, 1))
                .as("Product should be applicable to category campaign that targets this product's category's parent category")
                .isTrue();

        assertThat(campaign.isCampaignApplicable(shoppingCartService.getProducts()))
                .as("ShoppingCart that contains targeted category's product should be applicable to category campaign")
                .isTrue();

        assertThat(campaign.calculateDiscount(shoppingCartService.getProducts()))
                .as("Campaign discount should be correct")
                .isEqualTo(1);

        campaign = new ProductCampaign(apple, DiscountType.AMOUNT, 2);

        assertThat(campaign.isCampaignApplicable(apple, 1))
                .as("Product should be applicable to category campaign that targets this product's category's parent category")
                .isTrue();

        assertThat(campaign.isCampaignApplicable(shoppingCartService.getProducts()))
                .as("ShoppingCart that contains targeted category's product should be applicable to category campaign")
                .isTrue();

        assertThat(campaign.calculateDiscount(shoppingCartService.getProducts()))
                .as("Campaign discount should be correct")
                .isEqualTo(2);
    }

}
