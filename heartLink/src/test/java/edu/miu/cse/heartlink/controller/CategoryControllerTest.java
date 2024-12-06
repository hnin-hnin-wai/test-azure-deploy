package edu.miu.cse.heartlink.controller;

import edu.miu.cse.heartlink.dto.request.CategoryRequestDto;
import edu.miu.cse.heartlink.dto.response.CategoryResponseDto;
import edu.miu.cse.heartlink.service.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {
    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @Test
    void createCategory() {
        // Arrange
        CategoryRequestDto requestDto = new CategoryRequestDto("Electronics");
        CategoryResponseDto responseDto = new CategoryResponseDto(1, "Electronics");

        Mockito.when(categoryService.createCategory(requestDto)).thenReturn(Optional.of(responseDto));

        // Act
        ResponseEntity<CategoryResponseDto> response = categoryController.CreateCategory(requestDto);

        // Assert
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(responseDto, response.getBody());
    }

    @Test
    void findByCategoryName() {
        // Arrange
        String categoryName = "Electronics";
        CategoryResponseDto responseDto = new CategoryResponseDto(1, "Electronics");

        Mockito.when(categoryService.findByName(categoryName)).thenReturn(Optional.of(responseDto));

        // Act
        ResponseEntity<CategoryResponseDto> response = categoryController.findByCategoryName(categoryName);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(responseDto, response.getBody());
    }

    @Test
    void findAllCategories() {
        // Arrange
        List<CategoryResponseDto> responseDtos = List.of(
                new CategoryResponseDto(1, "Electronics"),
                new CategoryResponseDto(2, "Home Appliances")
        );

        Mockito.when(categoryService.findAllCategories()).thenReturn(responseDtos);

        // Act
        ResponseEntity<List<CategoryResponseDto>> response = categoryController.findAllCategories();

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(responseDtos.size(), response.getBody().size());
        Assertions.assertEquals(responseDtos, response.getBody());
    }

    @Test
    void updateCategory() {
        // Arrange
        String categoryName = "Electronics";
        CategoryRequestDto requestDto = new CategoryRequestDto("Updated Electronics");
        CategoryResponseDto responseDto = new CategoryResponseDto(1, "Updated Electronics");

        Mockito.when(categoryService.updateCategory(categoryName, requestDto)).thenReturn(Optional.of(responseDto));

        // Act
        ResponseEntity<CategoryResponseDto> response = categoryController.UpdateCategory(categoryName, requestDto);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(responseDto, response.getBody());
    }

    @Test
    void deleteCategory() {
        // Arrange
        String categoryName = "Electronics";

        Mockito.doNothing().when(categoryService).deleteCategory(categoryName);

        // Act
        ResponseEntity<CategoryResponseDto> response = categoryController.deleteCategory(categoryName);

        // Assert
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }
}