package edu.miu.cse.heartlink.service.impl;

import edu.miu.cse.heartlink.dto.request.CategoryRequestDto;
import edu.miu.cse.heartlink.dto.response.CategoryResponseDto;
import edu.miu.cse.heartlink.model.Category;
import edu.miu.cse.heartlink.repository.CategoryRepository;
import edu.miu.cse.heartlink.service.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryServiceImpl;

    @Test
    void createCategory() {
        // Arrange
        CategoryRequestDto requestDto = new CategoryRequestDto("Electronics");
        Category category = new Category();
        category.setId(1);
        category.setName("Electronics");

        Mockito.when(categoryRepository.save(Mockito.any(Category.class))).thenReturn(category);

        // Act
        Optional<CategoryResponseDto> responseDto = categoryServiceImpl.createCategory(requestDto);

        // Assert
        Assertions.assertTrue(responseDto.isPresent());
        Assertions.assertEquals("Electronics", responseDto.get().name());
        Assertions.assertEquals(1, responseDto.get().id());
    }

    @Test
    void findByName() {
        // Arrange
        String categoryName = "Electronics";
        Category category = new Category();
        category.setId(1);
        category.setName(categoryName);

        Mockito.when(categoryRepository.findByName(categoryName)).thenReturn(Optional.of(category));

        // Act
        Optional<CategoryResponseDto> responseDto = categoryServiceImpl.findByName(categoryName);

        // Assert
        Assertions.assertTrue(responseDto.isPresent());
        Assertions.assertEquals("Electronics", responseDto.get().name());
        Assertions.assertEquals(1, responseDto.get().id());
    }

    @Test
    void findAllCategories() {
        // Arrange
        List<Category> categories = List.of(
                new Category("Electronics"),
                new Category("Clothing")
        );

        Mockito.when(categoryRepository.findAll()).thenReturn(categories);

        // Act
        List<CategoryResponseDto> responseDtos = categoryServiceImpl.findAllCategories();

        // Assert
        Assertions.assertEquals(2, responseDtos.size());
        Assertions.assertEquals("Electronics", responseDtos.get(0).name());
        Assertions.assertEquals("Clothing", responseDtos.get(1).name());
    }

    @Test
    void updateCategory() {
        // Arrange
        String categoryName = "Electronics";
        CategoryRequestDto requestDto = new CategoryRequestDto("Updated Electronics");

        Category category = new Category();
        category.setId(1);
        category.setName(categoryName);

        Mockito.when(categoryRepository.findByName(categoryName)).thenReturn(Optional.of(category));
        Mockito.when(categoryRepository.save(Mockito.any(Category.class))).thenReturn(category);

        // Act
        Optional<CategoryResponseDto> responseDto = categoryServiceImpl.updateCategory(categoryName, requestDto);

        // Assert
        Assertions.assertTrue(responseDto.isPresent());
        Assertions.assertEquals("Updated Electronics", responseDto.get().name());
        Assertions.assertEquals(1, responseDto.get().id());
    }

    @Test
    void deleteCategory() {
        // Arrange
        String categoryName = "Electronics";

        Category category = new Category();
        category.setId(1);
        category.setName(categoryName);

        Mockito.when(categoryRepository.findByName(categoryName)).thenReturn(Optional.of(category));
        Mockito.doNothing().when(categoryRepository).deleteByName(categoryName);

        // Act
        categoryServiceImpl.deleteCategory(categoryName);

        // Assert
        Mockito.verify(categoryRepository, Mockito.times(1)).deleteByName(categoryName);
    }
}