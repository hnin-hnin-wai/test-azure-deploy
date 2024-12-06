package edu.miu.cse.heartlink.controller;

import edu.miu.cse.heartlink.dto.request.ItemClaimTransactionRequestDto;
import edu.miu.cse.heartlink.dto.response.ItemClaimResponseDto;
import edu.miu.cse.heartlink.dto.response.ItemClaimTransactionResponseDto;
import edu.miu.cse.heartlink.service.ItemClaimTransactionService;
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
class ItemClaimTransactionControllerTest {

    @Mock
    private ItemClaimTransactionService itemClaimTransactionService;

    @InjectMocks
    private ItemClaimTransactionController itemClaimTransactionController;


    @Test
    void createItemRequestTransaction() {
        // Arrange
        Integer itemRequestId = 101;
        ItemClaimTransactionResponseDto responseDto = new ItemClaimTransactionResponseDto(
                new ItemClaimResponseDto("PENDING", 1, 1001, 2001)
        );

        Mockito.when(itemClaimTransactionService.createItemRequestTransaction(itemRequestId))
                .thenReturn(Optional.of(responseDto));

        ItemClaimTransactionRequestDto requestDto = new ItemClaimTransactionRequestDto(itemRequestId);

        // Act
        ResponseEntity<ItemClaimTransactionResponseDto> response =
                itemClaimTransactionController.createItemRequestTransaction(requestDto);

        // Assert
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Status should be 201 CREATED");
        Assertions.assertNotNull(response.getBody(), "Response body should not be null");
        Assertions.assertEquals(responseDto, response.getBody(), "Response body should match the expected DTO");
    }

    @Test
    void getItemRequestTransaction() {
        // Arrange
        Integer userId = 2001;
        List<ItemClaimTransactionResponseDto> responseDtos = List.of(
                new ItemClaimTransactionResponseDto(
                        new ItemClaimResponseDto("ACCEPTED", 1, 1001, userId)
                ),
                new ItemClaimTransactionResponseDto(
                        new ItemClaimResponseDto("DECLINED", 2, 1002, userId)
                )
        );

        Mockito.when(itemClaimTransactionService.getAllItemRequestTransactionsByUserId(userId))
                .thenReturn(responseDtos);

        // Act
        ResponseEntity<List<ItemClaimTransactionResponseDto>> response =
                itemClaimTransactionController.getItemRequestTransaction(userId);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be 200 OK");
        Assertions.assertNotNull(response.getBody(), "Response body should not be null");
        Assertions.assertEquals(responseDtos.size(), response.getBody().size(), "Response size should match the expected size");
        Assertions.assertEquals(responseDtos, response.getBody(), "Response body should match the expected list of DTOs");
    }
}