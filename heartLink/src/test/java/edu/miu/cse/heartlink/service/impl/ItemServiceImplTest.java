package edu.miu.cse.heartlink.service.impl;

import edu.miu.cse.heartlink.dto.request.CategoryRequestDto;
import edu.miu.cse.heartlink.dto.request.ItemRequestDto;
import edu.miu.cse.heartlink.dto.response.ImageResponseDto;
import edu.miu.cse.heartlink.dto.response.ItemResponseDto;
import edu.miu.cse.heartlink.model.Category;
import edu.miu.cse.heartlink.model.Image;
import edu.miu.cse.heartlink.model.Item;
import edu.miu.cse.heartlink.model.User;
import edu.miu.cse.heartlink.repository.CategoryRepository;
import edu.miu.cse.heartlink.repository.ImageRepository;
import edu.miu.cse.heartlink.repository.ItemRepository;
import edu.miu.cse.heartlink.repository.UserRepository;
import edu.miu.cse.heartlink.service.S3Service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private S3Service s3Service;

    @InjectMocks
    private ItemServiceImpl itemServiceImpl;

    @Test
    void createItem() {
        // Arrange
        Category category = new Category();
        category.setId(1);
        category.setName("Electronics");

        User user = new User();
        user.setUserId(1001);

        Item item = new Item();
        item.setId(1);
        item.setName("Laptop");
        item.setCategory(category);
        item.setUser(user);

        ItemRequestDto requestDto = new ItemRequestDto("Laptop", new CategoryRequestDto("Electronics"), 1001);

        Mockito.when(categoryRepository.findByName("Electronics")).thenReturn(Optional.of(category));
        Mockito.when(userRepository.findById(1001)).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(item);

        // Act
        Optional<ItemResponseDto> result = itemServiceImpl.createItem(requestDto);

        // Assert
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("Laptop", result.get().name());
        Assertions.assertEquals("Electronics", result.get().categoryResponseDto().name());
        Assertions.assertEquals(1001, result.get().userId());

        Mockito.verify(itemRepository).save(Mockito.any(Item.class));
    }

    @Test
    void updateItem() {
        // Arrange
        Integer itemId = 1;

        Category category = new Category();
        category.setId(1);
        category.setName("Electronics");

        User user = new User();
        user.setUserId(1001);

        Item existingItem = new Item();
        existingItem.setId(itemId);
        existingItem.setName("Old Laptop");
        existingItem.setCategory(category);
        existingItem.setUser(user);

        Item updatedItem = new Item();
        updatedItem.setId(itemId);
        updatedItem.setName("Updated Laptop");
        updatedItem.setCategory(category);
        updatedItem.setUser(user);

        ItemRequestDto requestDto = new ItemRequestDto("Updated Laptop", new CategoryRequestDto("Electronics"), 1001);

        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(existingItem));
        Mockito.when(categoryRepository.findByName("Electronics")).thenReturn(Optional.of(category));
        Mockito.when(userRepository.findById(1001)).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(updatedItem);

        // Act
        Optional<ItemResponseDto> result = itemServiceImpl.updateItem(itemId, requestDto);

        // Assert
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("Updated Laptop", result.get().name());
        Assertions.assertEquals("Electronics", result.get().categoryResponseDto().name());
        Assertions.assertEquals(1001, result.get().userId());

        Mockito.verify(itemRepository).save(Mockito.any(Item.class));
    }

//    @Test
//    void uploadImages() throws IOException {
//        // Arrange
//        Integer itemId = 1;
//
//        Item item = new Item();
//        item.setId(itemId);
//        item.setName("Laptop");
//
//        MultipartFile imageFile = Mockito.mock(MultipartFile.class);
//        Mockito.when(imageFile.getOriginalFilename()).thenReturn("image1.jpg");
//        Mockito.when(imageFile.getContentType()).thenReturn("image/jpeg");
//        Mockito.when(imageFile.getBytes()).thenReturn(new byte[0]);
//
//        String s3Url = "https://s3.amazonaws.com/test-hhw-bucket/items/1/image1.jpg";
//
//        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
//        Mockito.when(s3Service.uploadImage(Mockito.anyString(), Mockito.anyString(), Mockito.any(byte[].class), Mockito.anyString()))
//                .thenReturn(s3Url);
//
//        // Act
//        List<ImageResponseDto> result = itemServiceImpl.uploadImages(itemId, List.of(imageFile));
//
//        // Assert
//        Assertions.assertFalse(result.isEmpty());
//        Assertions.assertEquals("image/jpeg", result.get(0).fileType());
//        Assertions.assertEquals(s3Url, result.get(0).url());
//
//        Mockito.verify(s3Service).uploadImage(Mockito.anyString(), Mockito.anyString(), Mockito.any(byte[].class), Mockito.anyString());
//    }

    @Test
    void deleteItem() {
        // Arrange
        Integer itemId = 1;

        Item item = new Item();
        item.setId(itemId);
        item.setName("Laptop");

        Image image = new Image();
        image.setUrl("https://s3.amazonaws.com/test-hhw-bucket/items/1/image1.jpg");
        image.setItem(item);

        item.setImages(List.of(image));

        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        // Act
        itemServiceImpl.deleteItem(itemId);

        // Assert
        Mockito.verify(itemRepository).delete(item);
        Mockito.verify(s3Service).deleteImage(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    void findAllItemsByDonar() {
        // Arrange
        Integer userId = 1001;

        Category category = new Category();
        category.setId(1);
        category.setName("Electronics");

        Item item = new Item();
        item.setId(1);
        item.setName("Laptop");
        item.setCategory(category);

        Mockito.when(itemRepository.findAllByUserId(userId)).thenReturn(List.of(item));

        // Act
        List<ItemResponseDto> result = itemServiceImpl.findAllItemsByDonar(userId);

        // Assert
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals("Laptop", result.get(0).name());
        Assertions.assertEquals("Electronics", result.get(0).categoryResponseDto().name());
        Assertions.assertEquals(userId, result.get(0).userId());

        Mockito.verify(itemRepository).findAllByUserId(userId);
    }

    @Test
    void findById() {
        // Arrange
        Integer itemId = 1;

        Category category = new Category();
        category.setId(1);
        category.setName("Electronics");

        Item item = new Item();
        item.setId(itemId);
        item.setName("Laptop");
        item.setCategory(category);

        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        // Act
        Optional<ItemResponseDto> result = itemServiceImpl.findById(itemId);

        // Assert
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("Laptop", result.get().name());
        Assertions.assertEquals("Electronics", result.get().categoryResponseDto().name());

        Mockito.verify(itemRepository).findById(itemId);
    }
}