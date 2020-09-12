package io.github.akadir.casestudy.product;

import io.github.akadir.casestudy.product.model.Category;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryTest {

    @Test
    public void testOneArgConstructor() {
        Category category = new Category("Food");

        assertThat(category)
                .as("Category should not be null")
                .isNotNull();
        assertThat(category.getTitle())
                .as("Category's title should be equal to one given in constructor")
                .isEqualTo("Food");
        assertThat(category.getParentCategory())
                .as("Parent category of category should be null")
                .isNull();
    }

    @Test
    public void testAllArgsConstructor() {
        Category parentCategory = new Category("Electronics");
        Category category = new Category("Computer", parentCategory);

        assertThat(category)
                .as("Category should not be null")
                .isNotNull();
        assertThat(category.getTitle())
                .as("Category's title should be equal to one given in constructor")
                .isEqualTo("Computer");
        assertThat(category.getParentCategory())
                .as("Parent category of category should be equal to created parent category")
                .isEqualTo(parentCategory);
    }

    @Test
    public void testSetters() {
        Category parentCategory = new Category("Electronics");
        Category category = new Category("Computer", parentCategory);

        Category anotherParentCategory = new Category("Computers");

        category.setTitle("Notebook");
        category.setParentCategory(anotherParentCategory);

        assertThat(category.getTitle())
                .as("Category's title should be changed after setTitle call")
                .isEqualTo("Notebook");

        assertThat(category.getParentCategory())
                .as("Category's title should be changed after setTitle call")
                .isEqualTo(anotherParentCategory);
    }

}
