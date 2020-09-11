package io.github.akadir.casestudy.model;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductTest {

    @Test
    public void testAllArgsConstructor() {
        Category food = new Category("Food");
        Product apple = new Product("Apple", 1, food);

        assertThat(apple)
                .as("Product should not be null")
                .isNotNull();

        assertThat(apple.getTitle())
                .as("Product title should be equal to one provided")
                .isEqualTo("Apple");

        assertThat(apple.getPrice())
                .as("Product price should be equal to one provided")
                .isEqualTo(1);

        assertThat(apple.getCategory())
                .as("Product category should be equal to one provided")
                .isEqualTo(food);
    }
}
