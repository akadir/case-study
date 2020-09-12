package io.github.akadir.casestudy.shopping.service;

import io.github.akadir.casestudy.discount.campaign.CategoryCampaign;
import io.github.akadir.casestudy.discount.campaign.ProductCampaign;
import io.github.akadir.casestudy.discount.campaign.base.Campaign;
import io.github.akadir.casestudy.discount.coupon.ConcreteCoupon;
import io.github.akadir.casestudy.discount.coupon.base.Coupon;
import io.github.akadir.casestudy.discount.enumeration.DiscountType;
import io.github.akadir.casestudy.product.model.Category;
import io.github.akadir.casestudy.product.model.Product;
import io.github.akadir.casestudy.shopping.service.impl.ConcreteShoppingCartServiceImpl;
import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ConcreteShoppingCartServiceTest {

    @Test
    public void whenClassCreatedThenExpectAllFieldsIsInitialisedAndNoException() {
        ShoppingCartService shoppingCart = new ConcreteShoppingCartServiceImpl();

        assertThat(shoppingCart)
                .as("Shopping cart should not be null")
                .isNotNull();

        assertThat(shoppingCart.getProducts())
                .as("Shopping cart's product list should not be null")
                .isNotNull();

        assertThat(shoppingCart.getProducts())
                .as("Shopping cart's product list should be empty")
                .isEmpty();
    }

    @Test
    public void whenProductAddedCartThenExpectCartHasExactAmountOfThatProduct() {
        ShoppingCartService shoppingCart = new ConcreteShoppingCartServiceImpl();

        Category category = new Category("foo");
        Product product = new Product("bar", 1, category);

        shoppingCart.addProduct(product);

        assertThat(shoppingCart.getProducts())
                .as("Shopping cart products size should be 1")
                .hasSize(1);

        assertThat(shoppingCart.getProducts())
                .as("Shopping cart product amount should be 1")
                .containsEntry(product, 1);

        shoppingCart.addProduct(product, 10);

        assertThat(shoppingCart.getProducts())
                .as("Shopping cart products size should be 1")
                .hasSize(1);

        assertThat(shoppingCart.getProducts())
                .as("Shopping cart product amount should be 11")
                .containsEntry(product, 11);
    }

    @Test
    public void whenNotPositiveAmountOfProductAddedToCartThenExpectProductCartNotChanged() {
        ShoppingCartService shoppingCart = new ConcreteShoppingCartServiceImpl();

        Category category = new Category("foo");
        Product product = new Product("bar", 1, category);

        shoppingCart.addProduct(product, 11);
        shoppingCart.addProduct(product, -1);
        shoppingCart.addProduct(product, -4);

        assertThat(shoppingCart.getProducts())
                .as("Shopping cart products size should be 1")
                .hasSize(1);

        assertThat(shoppingCart.getProducts())
                .as("Shopping cart product amount should be 11")
                .containsEntry(product, 11);
    }

    @Test
    public void whenProductRemovedFromCartThenExpectAmountOfProductDecreasedOrRemoved() {
        ShoppingCartService shoppingCart = new ConcreteShoppingCartServiceImpl();

        Category category = new Category("foo");
        Product product = new Product("bar", 1, category);

        shoppingCart.addProduct(product, 11);

        shoppingCart.removeProduct(product, 1);

        assertThat(shoppingCart.getProducts())
                .as("Shopping cart products size should be 1")
                .hasSize(1);

        assertThat(shoppingCart.getProducts())
                .as("Shopping cart product amount should be 10")
                .containsEntry(product, 10);

        shoppingCart.removeProduct(product, 10);

        assertThat(shoppingCart.getProducts())
                .as("Shopping cart products should be empty")
                .isEmpty();

        assertThat(shoppingCart.getProducts().get(product))
                .as("Shopping cart product amount should return null")
                .isNull();
    }

    @Test
    public void whenNonPositiveAmountOfProductRemovedFromCartThenExpectAmountOfProductNotChanged() {
        ShoppingCartService shoppingCart = new ConcreteShoppingCartServiceImpl();

        Category category = new Category("foo");
        Product product = new Product("bar", 1, category);

        shoppingCart.addProduct(product, 11);

        shoppingCart.removeProduct(product, -1);
        shoppingCart.removeProduct(product, -5);

        assertThat(shoppingCart.getProducts())
                .as("Shopping cart products size should be 1")
                .hasSize(1);

        assertThat(shoppingCart.getProducts())
                .as("Shopping cart product amount should be 11")
                .containsEntry(product, 11);
    }

    @Test
    public void whenRemoveProductCalledWithProductFromCartThenExpectProductRemovedFromCart() {
        ShoppingCartService shoppingCart = new ConcreteShoppingCartServiceImpl();

        Category category = new Category("foo");
        Product product = new Product("bar", 1, category);

        shoppingCart.addProduct(product, 11);

        shoppingCart.removeProduct(product);

        assertThat(shoppingCart.getProducts())
                .as("Shopping cart products should be empty")
                .isEmpty();
    }

    @Test
    public void whenChangingProductListFromUsingGetterExpectExceptionThrown() {
        ShoppingCartService shoppingCart = new ConcreteShoppingCartServiceImpl();
        Product foo = new Product("foo", 1, new Category("bar"));

        shoppingCart.addProduct(foo, 3);

        Map<Product, Integer> productsInShoppingCart = shoppingCart.getProducts();

        assertThatThrownBy(() -> productsInShoppingCart.put(foo, 1))
                .as("Adding new product through getter should not be permitted.")
                .isInstanceOf(UnsupportedOperationException.class);

        assertThatThrownBy(() -> productsInShoppingCart.remove(foo, 1))
                .as("Removing product through getter should not be permitted.")
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void whenCouponAppliedThenExpectDiscountToBeChanged() {
        ShoppingCartService shoppingCart = new ConcreteShoppingCartServiceImpl();
        Product foo = new Product("foo", 50, new Category("bar"));

        shoppingCart.addProduct(foo);

        Coupon coupon = new ConcreteCoupon(20d, DiscountType.AMOUNT, 5);

        assertThat(shoppingCart.applyCoupon(coupon))
                .as("Coupon should be applied")
                .isTrue();

        assertThat(shoppingCart.getDiscounts())
                .as("Discount amount should be 5 after coupon application")
                .isEqualTo(5);

        Campaign campaign = new CategoryCampaign(new Category("bar"), DiscountType.RATE, 10);

        assertThat(shoppingCart.applyCampaign(campaign))
                .as("Campaign should be applied")
                .isTrue();

        assertThat(shoppingCart.getDiscounts())
                .as("Discount amount should be 10 after campaign application")
                .isEqualTo(10);
    }

    @Test
    public void whenProductRemovedAfterCouponApplicationThenExpectDiscountToBeRecalculated() {
        ShoppingCartService shoppingCart = new ConcreteShoppingCartServiceImpl();
        Product foo = new Product("foo", 50, new Category("bar"));

        shoppingCart.addProduct(foo, 10);

        Coupon coupon = new ConcreteCoupon(20d, DiscountType.RATE, 10);

        assertThat(shoppingCart.applyCoupon(coupon))
                .as("Coupon should be applied")
                .isTrue();

        assertThat(shoppingCart.getDiscounts())
                .as("Discount should be 50 after coupon apply")
                .isEqualTo(50);

        shoppingCart.removeProduct(foo, 5);

        assertThat(shoppingCart.getDiscounts())
                .as("Discount should be changed after product remove")
                .isEqualTo(25);

        shoppingCart.removeProduct(foo, 5);

        assertThat(shoppingCart.getDiscounts())
                .as("Discount should be zero after all products removed from cart")
                .isZero();
    }

    @Test
    public void whenProductRemovedAfterCampaignApplicationThenExpectDiscountToBeRecalculated() {
        ShoppingCartService shoppingCart = new ConcreteShoppingCartServiceImpl();
        Product foo = new Product("foo", 50, new Category("bar"));

        shoppingCart.addProduct(foo, 10);

        Campaign categoryCampaign = new CategoryCampaign(new Category("bar"), DiscountType.RATE, 10);

        assertThat(shoppingCart.applyCampaign(categoryCampaign))
                .as("Campaign should be applied to cart")
                .isTrue();

        assertThat(shoppingCart.getDiscounts())
                .as("Discount should be 50")
                .isEqualTo(50);

        shoppingCart.removeProduct(foo, 5);

        assertThat(shoppingCart.getDiscounts())
                .as("Discount should be 25")
                .isEqualTo(25);

        shoppingCart.removeProduct(foo, 5);

        shoppingCart.addProduct(new Product("lorem", 50, new Category("not bar")));

        assertThat(shoppingCart.getDiscounts())
                .as("Discount should be 0 if there is no applicable product in cart")
                .isZero();
    }

    @Test
    public void whenApplyingCouponMoreThanCartPriceThenExpectDiscountIsZero() {
        ShoppingCartService shoppingCart = new ConcreteShoppingCartServiceImpl();
        Product foo = new Product("foo", 10, new Category("bar"));

        shoppingCart.addProduct(foo, 5);

        Coupon coupon = new ConcreteCoupon(20d, DiscountType.AMOUNT, 55);

        assertThat(shoppingCart.applyCoupon(coupon))
                .as("Coupon should not be applied if discount amount more than cart price")
                .isFalse();

        assertThat(shoppingCart.getDiscounts())
                .as("Discount should be zero")
                .isZero();

        shoppingCart.addProduct(foo, 2);

        assertThat(shoppingCart.applyCoupon(coupon))
                .as("Coupon should be applied after adding new products")
                .isTrue();

        assertThat(shoppingCart.getDiscounts())
                .as("Discount should be same as coupon discount amount")
                .isEqualTo(55);
    }

    @Test
    public void whenApplyingCampaignMoreThanCartPriceThenExpectDiscountIsZero() {
        ShoppingCartService shoppingCart = new ConcreteShoppingCartServiceImpl();
        Product foo = new Product("foo", 10, new Category("bar"));

        shoppingCart.addProduct(foo, 3);

        Campaign campaign = new ProductCampaign(foo, DiscountType.AMOUNT, 40);

        assertThat(shoppingCart.applyCampaign(campaign))
                .as("ProductCampaign should not be applied if discount amount more than product price")
                .isFalse();

        assertThat(shoppingCart.getDiscounts())
                .as("Discount should be zero")
                .isZero();

        shoppingCart.addProduct(foo, 2);

        assertThat(shoppingCart.applyCampaign(campaign))
                .as("ProductCampaign should not be applied if discount amount more than product price")
                .isFalse();

        assertThat(shoppingCart.getDiscounts())
                .as("Discount should be zero")
                .isZero();
    }

    @Test
    public void whenApplyingAnotherCouponOrCampaignThenExpectFalseResponse() {
        ShoppingCartService shoppingCart = new ConcreteShoppingCartServiceImpl();
        Product foo = new Product("foo", 10, new Category("bar"));

        shoppingCart.addProduct(foo, 3);

        Campaign campaign = new ProductCampaign(foo, DiscountType.AMOUNT, 5);
        Coupon coupon = new ConcreteCoupon(20, DiscountType.AMOUNT, 5);

        assertThat(shoppingCart.applyCampaign(campaign))
                .as("Campaign should be applied")
                .isTrue();

        assertThat(shoppingCart.getDiscounts())
                .as("Discount should be same as campaign discount amount")
                .isEqualTo(15);

        assertThat(shoppingCart.applyCoupon(coupon))
                .as("coupon should be applied")
                .isTrue();

        assertThat(shoppingCart.getDiscounts())
                .as("Discount should be updated as 20")
                .isEqualTo(20);

        Campaign anotherCampaign = new ProductCampaign(foo, DiscountType.RATE, 3);
        Coupon anotherCoupon = new ConcreteCoupon(5, DiscountType.RATE, 10);

        assertThat(shoppingCart.applyCampaign(anotherCampaign))
                .as("Campaign could not be applied as another campaign already applied")
                .isFalse();

        assertThat(shoppingCart.applyCoupon(anotherCoupon))
                .as("Coupon could not be applied as another coupon already applied")
                .isFalse();

        assertThat(shoppingCart.getDiscounts())
                .as("Discount should not be changed")
                .isEqualTo(20);
    }

    @Test
    public void whenApplyingNullCouponOrCampaignThenExpectFalse() {
        ShoppingCartService shoppingCart = new ConcreteShoppingCartServiceImpl();
        Product foo = new Product("foo", 10, new Category("bar"));

        shoppingCart.addProduct(foo, 3);

        assertThat(shoppingCart.applyCampaign(null))
                .as("Null campaign should not be applied")
                .isFalse();

        assertThat(shoppingCart.applyCoupon(null))
                .as("Null coupon should not be applied")
                .isFalse();

        assertThat(shoppingCart.getDiscounts())
                .as("Discount should not be changed after null coupon and campaign applications")
                .isZero();
    }
}
