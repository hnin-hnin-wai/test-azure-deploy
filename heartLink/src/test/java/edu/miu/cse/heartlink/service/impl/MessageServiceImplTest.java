package edu.miu.cse.heartlink.service.impl;

import edu.miu.cse.heartlink.dto.request.MessageRequestDto;
import edu.miu.cse.heartlink.dto.response.MessageResponseDto;
import edu.miu.cse.heartlink.model.Conversation;
import edu.miu.cse.heartlink.model.Message;
import edu.miu.cse.heartlink.model.User;
import edu.miu.cse.heartlink.repository.ConversationRepository;
import edu.miu.cse.heartlink.repository.MessageRepository;
import edu.miu.cse.heartlink.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceImplTest {
    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ConversationRepository conversationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MessageServiceImpl messageServiceImpl;

    @Test
    void addMessage() {
        // Arrange
        Integer conversationId = 1;
        Integer senderId = 1001;
        Integer receiverId = 1002;

        Conversation conversation = new Conversation();
        conversation.setId(conversationId);

        User sender = new User();
        sender.setUserId(senderId);

        User receiver = new User();
        receiver.setUserId(receiverId);

        Message message = new Message();
        message.setMessageId(1);
        message.setContent("Hello, how are you?");
        message.setConversation(conversation);
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setCreatedDateTime(LocalDateTime.now());

        MessageRequestDto requestDto = new MessageRequestDto(
                "Hello, how are you?", conversationId, senderId, receiverId
        );

        Mockito.when(conversationRepository.findById(conversationId)).thenReturn(Optional.of(conversation));
        Mockito.when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));
        Mockito.when(userRepository.findById(receiverId)).thenReturn(Optional.of(receiver));
        Mockito.when(messageRepository.save(Mockito.any(Message.class))).thenReturn(message);

        // Act
        Optional<MessageResponseDto> result = messageServiceImpl.addMessage(requestDto);

        // Assert
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("Hello, how are you?", result.get().content());
        Assertions.assertEquals(conversationId, result.get().conversationId());
        Assertions.assertEquals(senderId, result.get().senderId());
        Assertions.assertEquals(receiverId, result.get().receiverId());

        Mockito.verify(messageRepository).save(Mockito.any(Message.class));
    }

    @Test
    void getMessagesByConversationId() {
        // Arrange
        Integer conversationId = 1;
        int page = 0;
        int size = 10;

        Conversation conversation = new Conversation();
        conversation.setId(conversationId);

        Message message1 = new Message();
        message1.setMessageId(1);
        message1.setContent("Message 1");
        message1.setConversation(conversation);
        message1.setSender(new User(1001));
        message1.setReceiver(new User(1002));
        message1.setCreatedDateTime(LocalDateTime.now());

        Message message2 = new Message();
        message2.setMessageId(2);
        message2.setContent("Message 2");
        message2.setConversation(conversation);
        message2.setSender(new User(1001));
        message2.setReceiver(new User(1002));
        message2.setCreatedDateTime(LocalDateTime.now());

        Page<Message> messagePage = new PageImpl<>(List.of(message1, message2));

        Mockito.when(messageRepository.findByConversationId(conversationId, PageRequest.of(page, size)))
                .thenReturn(messagePage);

        // Act
        Page<MessageResponseDto> result = messageServiceImpl.getMessagesByConversationId(conversationId, page, size);

        // Assert
        Assertions.assertEquals(2, result.getContent().size());
        Assertions.assertEquals("Message 1", result.getContent().get(0).content());
        Assertions.assertEquals("Message 2", result.getContent().get(1).content());
        Assertions.assertEquals(conversationId, result.getContent().get(0).conversationId());

        Mockito.verify(messageRepository).findByConversationId(conversationId, PageRequest.of(page, size));
    }
}