package edu.miu.cse.heartlink.controller;

import edu.miu.cse.heartlink.dto.request.CategoryRequestDto;
import edu.miu.cse.heartlink.dto.request.ItemRequestDto;
import edu.miu.cse.heartlink.dto.response.CategoryResponseDto;
import edu.miu.cse.heartlink.dto.response.ImageResponseDto;
import edu.miu.cse.heartlink.dto.response.ItemResponseDto;
import edu.miu.cse.heartlink.service.ItemService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    @Test
    void createItem() {
        // Arrange
        ItemRequestDto requestDto = new ItemRequestDto("Item1", new CategoryRequestDto("Category1"), 1);
        ItemResponseDto responseDto = new ItemResponseDto("Item1", new CategoryResponseDto(1, "Category1"), 1);

        Mockito.when(itemService.createItem(requestDto)).thenReturn(Optional.of(responseDto));

        // Act
        ResponseEntity<ItemResponseDto> response = itemController.createItem(requestDto);

        // Assert
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(responseDto, response.getBody());
    }

    @Test
    void updateItem() {
        // Arrange
        Integer itemId = 1;
        ItemRequestDto requestDto = new ItemRequestDto("UpdatedItem", new CategoryRequestDto("UpdatedCategory"), 1);
        ItemResponseDto responseDto = new ItemResponseDto("UpdatedItem", new CategoryResponseDto(1, "UpdatedCategory"), 1);

        Mockito.when(itemService.updateItem(itemId, requestDto)).thenReturn(Optional.of(responseDto));

        // Act
        ResponseEntity<ItemResponseDto> response = itemController.updateItem(itemId, requestDto);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(responseDto, response.getBody());
    }

    @Test
    void uploadImages() {
        // Arrange
        Integer itemId = 1;
        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
        List<MultipartFile> images = List.of(mockFile);

        ImageResponseDto imageResponseDto = new ImageResponseDto("png","image-url");
        Mockito.when(itemService.uploadImages(itemId, images)).thenReturn(List.of(imageResponseDto));

        // Act
        ResponseEntity<List<ImageResponseDto>> response = itemController.uploadImages(itemId, images);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(1, response.getBody().size());
        Assertions.assertEquals(imageResponseDto, response.getBody().get(0));
    }

    @Test
    void updateImages() {
        // Arrange
        Integer itemId = 1;
        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
        List<MultipartFile> images = List.of(mockFile);

        ImageResponseDto imageResponseDto = new ImageResponseDto("png","updated-image-url");
        Mockito.when(itemService.updateImages(itemId, images)).thenReturn(List.of(imageResponseDto));

        // Act
        ResponseEntity<List<ImageResponseDto>> response = itemController.updateImages(itemId, images);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(1, response.getBody().size());
        Assertions.assertEquals(imageResponseDto, response.getBody().get(0));
    }

    @Test
    void deleteItem() {
        // Arrange
        Integer itemId = 1;
        ItemResponseDto responseDto = new ItemResponseDto("Item1", new CategoryResponseDto(1, "Category1"), 1);

        Mockito.when(itemService.findById(itemId)).thenReturn(Optional.of(responseDto));
        Mockito.doNothing().when(itemService).deleteItem(itemId);

        // Act
        ResponseEntity<Void> response = itemController.deleteItem(itemId);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    void findByItemId() {
        // Arrange
        Integer itemId = 1;
        ItemResponseDto responseDto = new ItemResponseDto("Item1", new CategoryResponseDto(1, "Category1"), 1);

        Mockito.when(itemService.findById(itemId)).thenReturn(Optional.of(responseDto));

        // Act
        ResponseEntity<ItemResponseDto> response = itemController.findByItemId(itemId);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(responseDto, response.getBody());
    }

    @Test
    void findAllItems() {
        // Arrange
        Integer userId = 1;
        List<ItemResponseDto> responseDtos = List.of(
                new ItemResponseDto("Item1", new CategoryResponseDto(1, "Category1"), 1),
                new ItemResponseDto("Item2", new CategoryResponseDto(2, "Category2"), 1)
        );

        Mockito.when(itemService.findAllItemsByDonar(userId)).thenReturn(responseDtos);

        // Act
        ResponseEntity<List<ItemResponseDto>> response = itemController.findAllItems(userId);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(responseDtos.size(), response.getBody().size());
        Assertions.assertEquals(responseDtos, response.getBody());
    }
}