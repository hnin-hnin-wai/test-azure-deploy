package edu.miu.cse.heartlink.controller;

import edu.miu.cse.heartlink.dto.request.MessageRequestDto;
import edu.miu.cse.heartlink.dto.response.MessageResponseDto;
import edu.miu.cse.heartlink.service.MessageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MessageControllerTest {

    @Mock
    private MessageService messageService;

    @InjectMocks
    private MessageController messageController;

    @Test
    void addMessage() {
        // Arrange
        MessageRequestDto requestDto = new MessageRequestDto("Hello", 1, 2, 3);
        MessageResponseDto responseDto = new MessageResponseDto(1, "Hello", LocalDateTime.now(), 1, 2, 3);

        Mockito.when(messageService.addMessage(requestDto)).thenReturn(Optional.of(responseDto));

        // Act
        ResponseEntity<Optional<MessageResponseDto>> response = messageController.addMessage(requestDto);

        // Assert
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().isPresent());
        Assertions.assertEquals(responseDto, response.getBody().get());
    }

    @Test
    void getMessages() {
        // Arrange
        Integer conversationId = 1;
        int page = 0;
        int size = 10;

        MessageResponseDto message1 = new MessageResponseDto(1, "Hello", LocalDateTime.now(), conversationId, 2, 3);
        MessageResponseDto message2 = new MessageResponseDto(2, "Hi", LocalDateTime.now(), conversationId, 3, 2);

        Page<MessageResponseDto> messages = new PageImpl<>(List.of(message1, message2));

        Mockito.when(messageService.getMessagesByConversationId(conversationId, page, size)).thenReturn(messages);

        // Act
        ResponseEntity<Page<MessageResponseDto>> response = messageController.getMessages(conversationId, page, size);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(2, response.getBody().getContent().size());
        Assertions.assertEquals(message1, response.getBody().getContent().get(0));
        Assertions.assertEquals(message2, response.getBody().getContent().get(1));
    }

}