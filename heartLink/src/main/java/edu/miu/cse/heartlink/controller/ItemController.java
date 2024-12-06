package edu.miu.cse.heartlink.controller;

import edu.miu.cse.heartlink.dto.request.ItemRequestDto;
import edu.miu.cse.heartlink.dto.response.CategoryResponseDto;
import edu.miu.cse.heartlink.dto.response.ImageResponseDto;
import edu.miu.cse.heartlink.dto.response.ItemResponseDto;
import edu.miu.cse.heartlink.service.CategoryService;
import edu.miu.cse.heartlink.service.ItemService;
import edu.miu.cse.heartlink.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final S3Service s3Service;

    @PostMapping
    public ResponseEntity<ItemResponseDto> createItem(@RequestBody ItemRequestDto itemRequestDto) {
        Optional<ItemResponseDto> itemResponseDto = itemService.createItem(itemRequestDto);
        if (itemResponseDto.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(itemResponseDto.get());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }


    @PutMapping("/{itemId}")
    public ResponseEntity<ItemResponseDto> updateItem(
            @PathVariable Integer itemId,
            @RequestBody ItemRequestDto requestDto) {
        Optional<ItemResponseDto> itemResponseDto = itemService.updateItem(itemId, requestDto);
        if (itemResponseDto.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(itemResponseDto.get());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }


    @PostMapping(value = "/{itemId}/upload-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<ImageResponseDto>> uploadImages(
            @PathVariable Integer itemId,
            @RequestPart("images") List<MultipartFile> images
    ) {
        List<ImageResponseDto> imageResponseDtos = itemService.uploadImages(itemId, images);
        return ResponseEntity.ok(imageResponseDtos);
    }

    //updateImages
    @PutMapping(value = "/{itemId}/update-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<ImageResponseDto>> updateImages(
            @PathVariable Integer itemId,
            @RequestPart("images") List<MultipartFile> images
    ) {
        List<ImageResponseDto> updatedImages = itemService.updateImages(itemId, images);
        return ResponseEntity.ok(updatedImages);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Integer itemId) {
        Optional<ItemResponseDto> itemResponseDto=itemService.findById(itemId);
        if (itemResponseDto.isPresent()) {
            itemService.deleteItem(itemId);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity<ItemResponseDto> findByItemId(@PathVariable Integer itemId) {
        Optional<ItemResponseDto> itemResponseDto=itemService.findById(itemId);
        return ResponseEntity.status(HttpStatus.OK).body(itemResponseDto.get());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<ItemResponseDto>> findAllItems(@PathVariable Integer userId) {
        List<ItemResponseDto> items = itemService.findAllItemsByDonar(userId);
        return ResponseEntity.status(HttpStatus.OK).body(items);
    }

}
