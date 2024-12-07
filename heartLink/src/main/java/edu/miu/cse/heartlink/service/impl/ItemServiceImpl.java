package edu.miu.cse.heartlink.service.impl;

import edu.miu.cse.heartlink.dto.request.ItemRequestDto;
import edu.miu.cse.heartlink.dto.response.CategoryResponseDto;
import edu.miu.cse.heartlink.dto.response.ImageResponseDto;
import edu.miu.cse.heartlink.dto.response.ItemResponseDto;
import edu.miu.cse.heartlink.exception.item.ItemNotFoundException;
import edu.miu.cse.heartlink.model.Category;
import edu.miu.cse.heartlink.model.Image;
import edu.miu.cse.heartlink.model.Item;
import edu.miu.cse.heartlink.model.User;
import edu.miu.cse.heartlink.repository.CategoryRepository;
import edu.miu.cse.heartlink.repository.ImageRepository;
import edu.miu.cse.heartlink.repository.ItemRepository;
import edu.miu.cse.heartlink.repository.UserRepository;
import edu.miu.cse.heartlink.service.ItemService;
import edu.miu.cse.heartlink.service.S3Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    @Override
    public Optional<ItemResponseDto> createItem(ItemRequestDto itemRequestDto) {
        // Fetch category
        Optional<Category> category = categoryRepository.findByName(itemRequestDto.categoryRequestDto().name());
        if (category.isEmpty()) {
            throw new IllegalArgumentException("Invalid category");
        }

        // Prepare the item entity
        Item item = new Item();
        item.setName(itemRequestDto.name());
        item.setCategory(category.get());

        // Fetch user by ID
        Optional<User> user = userRepository.findById(itemRequestDto.userId());
        if (user.isEmpty()) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        item.setUser(user.get());

        // Save item
        Item savedItem = itemRepository.save(item);

         ItemResponseDto itemResponseDto=new ItemResponseDto(
                savedItem.getName(),
                new CategoryResponseDto(category.get().getId(), category.get().getName()),
                //null, // No images at this stage
                savedItem.getUser().getUserId()
        );
         return Optional.of(itemResponseDto);
    }

    @Override
    public Optional<ItemResponseDto> updateItem(Integer itemId, ItemRequestDto itemRequestDto) {
        // Fetch the existing item by ID
        Optional<Item> existingItemOpt = itemRepository.findById(itemId);
        if (existingItemOpt.isEmpty()) {
            throw new ItemNotFoundException("Item with ID " + itemId + " not found");
        }

        // Get the existing item
        Item existingItem = existingItemOpt.get();

        // Update the category if needed
        Optional<Category> categoryOpt = categoryRepository.findByName(itemRequestDto.categoryRequestDto().name());
        if (categoryOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid category");
        }
        existingItem.setCategory(categoryOpt.get());

        // Update the item name
        existingItem.setName(itemRequestDto.name());

        // Update the user if needed
        Optional<User> userOpt = userRepository.findById(itemRequestDto.userId());
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        existingItem.setUser(userOpt.get());

        // Save the updated item
        Item updatedItem = itemRepository.save(existingItem);

        // Prepare the response
        ItemResponseDto itemResponseDto = new ItemResponseDto(
                updatedItem.getName(),
                new CategoryResponseDto(
                        updatedItem.getCategory().getId(),
                        updatedItem.getCategory().getName()
                ),
                updatedItem.getUser().getUserId()
        );

        return Optional.of(itemResponseDto);
    }

    @Override
    public List<ImageResponseDto> uploadImages(Integer itemId, List<MultipartFile> images) {
        // Fetch the item
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isEmpty()) {
            throw new IllegalArgumentException("Item not found");
        }

        // Save images to S3 and database
        List<Image> imageEntities = images.stream().map(imageFile -> {
            try {
                // Upload image to S3
                String s3Key = "items/" + itemId + "/" + imageFile.getOriginalFilename()+ "_" + System.currentTimeMillis();
                String url = s3Service.uploadImage("test-hhw-bucket", s3Key, imageFile.getBytes(), imageFile.getContentType());

                // Save image metadata to the database
                Image image = new Image();
                image.setFileType(imageFile.getContentType());
                image.setUrl(url);
                image.setItem(item.get());
                return imageRepository.save(image);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload image: " + imageFile.getOriginalFilename(), e);
            }
        }).toList();

        // Prepare response
        return imageEntities.stream()
                .map(image -> new ImageResponseDto(image.getFileType(), image.getUrl()))
                .toList();
    }

    //updateImages
    @Override
    public List<ImageResponseDto> updateImages(Integer itemId, List<MultipartFile> images) {

        // Fetch the item
        Optional<Item> itemOpt = itemRepository.findById(itemId);
        if (itemOpt.isEmpty()) {
            throw new IllegalArgumentException("Item not found");
        }
        Item item = itemOpt.get();

        // Fetch existing images associated with the item
        List<Image> existingImages = imageRepository.findByItemId(itemId);

        // Delete existing images from S3 and database
        existingImages.forEach(existingImage -> {

            // Extract the filename from the URL (including the timestamp)
            String fileNameWithTimestamp = existingImage.getUrl().substring(existingImage.getUrl().lastIndexOf("/") + 1);
            System.out.println("FileNameWithTimestamp" + fileNameWithTimestamp);

            // Reconstruct the full S3 key using the itemId and extracted filename
            String s3Key = "items/" + itemId + "/" + fileNameWithTimestamp;
            s3Service.deleteImage("test-hhw-bucket", s3Key);

            imageRepository.deleteByItemId(existingImage.getItem().getId());
        });

        // Upload new images to S3 and save them to the database
        List<Image> newImages = images.stream().map(imageFile -> {
            try {
                // Upload image to S3
                String s3Key = "items/" + itemId + "/" + imageFile.getOriginalFilename();
                String url = s3Service.uploadImage("test-hhw-bucket", s3Key, imageFile.getBytes(), imageFile.getContentType());

                // Create a new image entity
                Image image = new Image();
                image.setFileType(imageFile.getContentType());
                image.setUrl(url);
                image.setItem(item);
                return imageRepository.save(image);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload image: " + imageFile.getOriginalFilename(), e);
            }
        }).toList();

        // Prepare response DTO
        return newImages.stream()
                .map(image -> new ImageResponseDto(image.getFileType(), image.getUrl()))
                .toList();
    }

    @Override
    @Transactional
    public void deleteItem(Integer itemId) {
        // Step 1: Find the item by its ID
        Optional<Item> itemOpt = itemRepository.findById(itemId);

        // Step 2: If the item exists, handle deletion logic
        if (itemOpt.isPresent()) {
            Item item = itemOpt.get();

            // Step 2.1: Delete associated images from S3
            item.getImages().forEach(image -> {
                String s3Key = "items/" + item.getId() + "/" + extractFileNameFromUrl(image.getUrl());
                try {
                    s3Service.deleteImage("test-hhw-bucket", s3Key);
                } catch (Exception e) {
                    // Log the error but proceed with deletion
                    System.err.println("Error deleting image from S3: " + e.getMessage());
                }
            });

            // Step 2.2: Delete the item from the repository
            itemRepository.delete(item);

        } else {
            // Step 3: Handle the case where the item is not found
            throw new ItemNotFoundException("Item with ID " + itemId + " not found");
        }
    }

    @Override
    public List<ItemResponseDto> findAllItemsByDonar(Integer userId) {
        // Fetch items associated with the given userId
        List<Item> items = itemRepository.findAllByUserId(userId);

        // Map the items to a list of ItemResponseDto
        return items.stream().map(item -> {
            // Map category
            CategoryResponseDto categoryResponseDto = new CategoryResponseDto(
                    item.getCategory().getId(),
                    item.getCategory().getName()
            );

            // Map images
            List<ImageResponseDto> imageResponseDtos = item.getImages().stream()
                    .map(image -> {
                        // Use the URL stored in the database
                        String url = image.getUrl();
                        if (url == null || url.isEmpty()) {
                            throw new RuntimeException("Image URL is missing for item ID: " + item.getId());
                        }

                        System.out.println("Image URL: " + url);
                        return new ImageResponseDto(image.getFileType(), url);
                    }).toList();

            // Map item to ItemResponseDto
            return new ItemResponseDto(
                    item.getName(),
                    categoryResponseDto,
                    //imageResponseDtos,
                    userId
            );
        }).toList();
    }

    @Override
    public Optional<ItemResponseDto> findById(Integer itemId) {
        // Step 1: Find the item by its ID
        Optional<Item> itemOpt = itemRepository.findById(itemId);

        if (itemOpt.isPresent()) {
            Item item = itemOpt.get();

            // Step 2.1: Map category
            CategoryResponseDto categoryResponseDto = Optional.ofNullable(item.getCategory())
                    .map(category -> new CategoryResponseDto(
                            category.getId(),
                            category.getName()
                    ))
                    .orElseThrow(() -> new RuntimeException("Category not found for item ID: " + itemId));

            // Step 2.2: Map images
            List<ImageResponseDto> imageResponseDtos = item.getImages().stream()
                    .distinct() // Ensure no duplicate images
                    .map(image -> {
                        String publicUrl = image.getUrl(); // Use the existing public URL stored in the database
                        if (publicUrl == null || publicUrl.isEmpty()) {
                            throw new RuntimeException("Image URL is missing for item ID: " + itemId);
                        }
                        return new ImageResponseDto(image.getFileType(), publicUrl);
                    })
                    .toList();

            // Step 2.3: Create and return the ItemResponseDto
            ItemResponseDto itemResponseDto = new ItemResponseDto(
                    item.getName(),
                    categoryResponseDto,
                    //imageResponseDtos,
                    item.getUser() != null ? item.getUser().getUserId() : null
            );

            return Optional.of(itemResponseDto);
        }

        // Step 3: If the item is not found, throw an exception
        throw new ItemNotFoundException("Item with ID " + itemId + " not found");
    }

    // Helper method to extract the file name from a URL
    private String extractFileNameFromUrl(String url) {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("URL cannot be null or empty");
        }
        return url.substring(url.lastIndexOf("/") + 1);
    }

}
