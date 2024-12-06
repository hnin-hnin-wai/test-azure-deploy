package edu.miu.cse.heartlink.controller;

import edu.miu.cse.heartlink.dto.request.ItemClaimRequestDto;
import edu.miu.cse.heartlink.dto.response.CategoryResponseDto;
import edu.miu.cse.heartlink.dto.response.ItemClaimResponseDto;
import edu.miu.cse.heartlink.dto.response.ItemResponseDto;
import edu.miu.cse.heartlink.service.ItemClaimService;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ItemClaimControllerTest {

    @Mock
    private ItemClaimService itemClaimService;

    @InjectMocks
    private ItemClaimController itemClaimController;

    @Test
    void findAllItemRequest() {
        // Arrange
        List<ItemClaimResponseDto> responseDtos = List.of(
                new ItemClaimResponseDto("PENDING", 1, 1001, 2001),
                new ItemClaimResponseDto("ACCEPTED", 2, 1002, 2002)
        );

        Mockito.when(itemClaimService.findAllItemRequest()).thenReturn(responseDtos);

        // Act
        ResponseEntity<List<ItemClaimResponseDto>> response = itemClaimController.findAllItemRequest();

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(responseDtos, response.getBody());
    }

    @Test
    void findItemRequestById() {
        // Arrange
        Integer itemId = 1;
        List<ItemClaimResponseDto> responseDtos = List.of(
                new ItemClaimResponseDto("PENDING", 1, 1001, 2001)
        );

        Mockito.when(itemClaimService.findItemRequestByItemId(itemId)).thenReturn(responseDtos);

        // Act
        ResponseEntity<List<ItemClaimResponseDto>> response = itemClaimController.findItemRequestById(itemId);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(responseDtos, response.getBody());
    }

    @Test
    void createItemRequest() {
        // Arrange
        ItemClaimRequestDto requestDto = new ItemClaimRequestDto("PENDING", 1, 1001, 2001);
        ItemClaimResponseDto responseDto = new ItemClaimResponseDto("PENDING", 1, 1001, 2001);

        Mockito.when(itemClaimService.createItemRequest(requestDto)).thenReturn(Optional.of(responseDto));

        // Act
        ResponseEntity<ItemClaimResponseDto> response = itemClaimController.createItemRequest(requestDto);

        // Assert
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(responseDto, response.getBody());
    }

    @Test
    void getAcceptedItemsByReceiverId() {
        // Arrange
        Integer receiverId = 1;

        List<ItemResponseDto> mockItemResponseDtos = List.of(
                new ItemResponseDto("Item1", new CategoryResponseDto(101, "Category1"), 201),
                new ItemResponseDto("Item2", new CategoryResponseDto(102, "Category2"), 202)
        );

        Mockito.when(itemClaimService.findAllAcceptedItemsByReceiverId(receiverId))
                .thenReturn(mockItemResponseDtos);

        // Act
        ResponseEntity<List<ItemResponseDto>> response = itemClaimController.getAcceptedItemsByReceiverId(receiverId);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(2, response.getBody().size());
        Assertions.assertEquals("Item1", response.getBody().get(0).name());
        Assertions.assertEquals("Category1", response.getBody().get(0).categoryResponseDto().name());
        Assertions.assertEquals(201, response.getBody().get(0).userId());
        Assertions.assertEquals("Item2", response.getBody().get(1).name());
        Assertions.assertEquals("Category2", response.getBody().get(1).categoryResponseDto().name());
        Assertions.assertEquals(202, response.getBody().get(1).userId());

        // Verify interactions
        Mockito.verify(itemClaimService, Mockito.times(1)).findAllAcceptedItemsByReceiverId(receiverId);
    }

}