package edu.miu.cse.heartlink.service.impl;

import edu.miu.cse.heartlink.dto.request.ConversationRequestDto;
import edu.miu.cse.heartlink.dto.response.ConversationResponseDto;
import edu.miu.cse.heartlink.model.Conversation;
import edu.miu.cse.heartlink.model.Item;
import edu.miu.cse.heartlink.model.User;
import edu.miu.cse.heartlink.repository.ConversationRepository;
import edu.miu.cse.heartlink.repository.ItemRepository;
import edu.miu.cse.heartlink.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ConversationServiceImplTest {

    @Mock
    private ConversationRepository conversationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ConversationServiceImpl conversationServiceImpl;

    @Test
    void createConversation_whenConversationAlreadyExists() {
        // Arrange
        ConversationRequestDto requestDto = new ConversationRequestDto(
                "Test Conversation",
                LocalDateTime.now(),
                1,
                2,
                101
        );

        Conversation existingConversation = new Conversation();
        existingConversation.setTitle("Test Conversation");
        existingConversation.setCreatedDateTime(LocalDateTime.now());
        existingConversation.setSender(new User(1));
        existingConversation.setReceiver(new User(2));
        existingConversation.setItem(new Item(101));

        Mockito.when(conversationRepository.findBySenderReceiverAndItem(1, 2, 101))
                .thenReturn(Optional.of(existingConversation));

        // Act
        Optional<ConversationResponseDto> responseDto = conversationServiceImpl.createConversation(requestDto);

        // Assert
        Assertions.assertTrue(responseDto.isPresent());
        Assertions.assertEquals("Test Conversation", responseDto.get().title());
        Assertions.assertEquals(1, responseDto.get().senderId());
        Assertions.assertEquals(2, responseDto.get().receiverId());
        Assertions.assertEquals(101, responseDto.get().itemId());
    }

    @Test
    void createConversation_whenNewConversationIsCreated() {
        // Arrange
        ConversationRequestDto requestDto = new ConversationRequestDto(
                "New Conversation",
                LocalDateTime.now(),
                1,
                2,
                102
        );

        User sender = new User(1);
        User receiver = new User(2);
        Item item = new Item(102);

        Conversation newConversation = new Conversation();
        newConversation.setTitle("New Conversation");
        newConversation.setCreatedDateTime(LocalDateTime.now());
        newConversation.setSender(sender);
        newConversation.setReceiver(receiver);
        newConversation.setItem(item);

        Mockito.when(conversationRepository.findBySenderReceiverAndItem(1, 2, 102))
                .thenReturn(Optional.empty());
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(sender));
        Mockito.when(userRepository.findById(2)).thenReturn(Optional.of(receiver));
        Mockito.when(itemRepository.findById(102)).thenReturn(Optional.of(item));
        Mockito.when(conversationRepository.save(Mockito.any(Conversation.class))).thenReturn(newConversation);

        // Act
        Optional<ConversationResponseDto> responseDto = conversationServiceImpl.createConversation(requestDto);

        // Assert
        Assertions.assertTrue(responseDto.isPresent());
        Assertions.assertEquals("New Conversation", responseDto.get().title());
        Assertions.assertEquals(1, responseDto.get().senderId());
        Assertions.assertEquals(2, responseDto.get().receiverId());
        Assertions.assertEquals(102, responseDto.get().itemId());
    }

    @Test
    void findConversationByConversationId_whenConversationExists() {
        // Arrange
        Integer conversationId = 1;
        Conversation conversation = new Conversation();
        conversation.setTitle("Existing Conversation");
        conversation.setCreatedDateTime(LocalDateTime.now());
        conversation.setSender(new User(1));
        conversation.setReceiver(new User(2));
        conversation.setItem(new Item(101));

        Mockito.when(conversationRepository.findById(conversationId))
                .thenReturn(Optional.of(conversation));

        // Act
        Optional<ConversationResponseDto> responseDto = conversationServiceImpl.findConversationByConversationId(conversationId);

        // Assert
        Assertions.assertTrue(responseDto.isPresent());
        Assertions.assertEquals("Existing Conversation", responseDto.get().title());
        Assertions.assertEquals(1, responseDto.get().senderId());
        Assertions.assertEquals(2, responseDto.get().receiverId());
        Assertions.assertEquals(101, responseDto.get().itemId());
    }

    @Test
    void findConversationByConversationId_whenConversationDoesNotExist() {
        // Arrange
        Integer conversationId = 1;

        Mockito.when(conversationRepository.findById(conversationId))
                .thenReturn(Optional.empty());

        // Act
        Optional<ConversationResponseDto> responseDto = conversationServiceImpl.findConversationByConversationId(conversationId);

        // Assert
        Assertions.assertTrue(responseDto.isEmpty());
    }
}