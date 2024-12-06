package edu.miu.cse.heartlink.controller;

import edu.miu.cse.heartlink.dto.request.ConversationRequestDto;
import edu.miu.cse.heartlink.dto.response.ConversationResponseDto;
import edu.miu.cse.heartlink.service.ConversationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ConversationControllerTest {

    @Mock
    private ConversationService conversationService;

    @InjectMocks
    private ConversationController conversationController;

    @Test
    void createConversation() {
        // Arrange
        ConversationRequestDto requestDto = new ConversationRequestDto(
                "Test Conversation",
                LocalDateTime.of(2023, 12, 4, 10, 30),
                1,
                2,
                101
        );

        ConversationResponseDto responseDto = new ConversationResponseDto(
                "Test Conversation",
                LocalDateTime.of(2023, 12, 4, 10, 30),
                1,
                2,
                101
        );

        Mockito.when(conversationService.createConversation(requestDto))
                .thenReturn(Optional.of(responseDto));

        // Act
        ResponseEntity<ConversationResponseDto> response = conversationController.CreateConversation(requestDto);

        // Assert
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(responseDto, response.getBody());
    }

    @Test
    void findByConversationId() {
        // Arrange
        int conversationId = 1;
        ConversationResponseDto responseDto = new ConversationResponseDto(
                "Test Conversation",
                LocalDateTime.of(2023, 12, 4, 10, 30),
                1,
                2,
                101
        );

        Mockito.when(conversationService.findConversationByConversationId(conversationId))
                .thenReturn(Optional.of(responseDto));

        // Act
        ResponseEntity<ConversationResponseDto> response = conversationController.findByConversationId(conversationId);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(responseDto, response.getBody());
    }
}