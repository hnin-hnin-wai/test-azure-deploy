package edu.miu.cse.heartlink.service.impl;

import edu.miu.cse.heartlink.dto.request.MessageRequestDto;
import edu.miu.cse.heartlink.dto.response.MessageResponseDto;
import edu.miu.cse.heartlink.model.Conversation;
import edu.miu.cse.heartlink.model.Message;
import edu.miu.cse.heartlink.model.User;
import edu.miu.cse.heartlink.repository.ConversationRepository;
import edu.miu.cse.heartlink.repository.MessageRepository;
import edu.miu.cse.heartlink.repository.UserRepository;
import edu.miu.cse.heartlink.service.MessageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;

    @Transactional
    public Optional<MessageResponseDto> addMessage(MessageRequestDto messageRequestDto) {
        // Validate and set conversation
        Conversation conversation = conversationRepository.findById(messageRequestDto.conversationId())
                .orElseThrow(() -> new RuntimeException("Conversation not found with ID: " + messageRequestDto.conversationId()));

        // Validate and set sender
        User sender = userRepository.findById(messageRequestDto.senderId())
                .orElseThrow(() -> new RuntimeException("Sender not found with ID: " + messageRequestDto.senderId()));

        // Validate and set receiver
        User receiver = userRepository.findById(messageRequestDto.receiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found with ID: " + messageRequestDto.receiverId()));

        // Create and save message
        Message message = new Message();
        message.setContent(messageRequestDto.content());
        message.setConversation(conversation);
        message.setSender(sender);
        message.setReceiver(receiver);

        Message savedMessage = messageRepository.save(message);
        MessageResponseDto messageResponseDto= new MessageResponseDto(
                savedMessage.getMessageId(),
                savedMessage.getContent(),
                savedMessage.getCreatedDateTime(),
                savedMessage.getConversation().getId(),
                savedMessage.getSender().getUserId(),
                savedMessage.getReceiver().getUserId()
        );
        return Optional.of(messageResponseDto);
    }

    // Fetch messages with pagination
    public Page<MessageResponseDto> getMessagesByConversationId(Integer conversationId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Message> messagesPage = messageRepository.findByConversationId(conversationId, pageable);
        return messagesPage.map(message -> new MessageResponseDto(
                message.getMessageId(),
                message.getContent(),
                message.getCreatedDateTime(),
                message.getConversation().getId(),
                message.getSender().getUserId(),
                message.getReceiver().getUserId()
        ));
    }
}
