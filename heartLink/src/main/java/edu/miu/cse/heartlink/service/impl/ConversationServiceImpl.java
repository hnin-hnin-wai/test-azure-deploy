package edu.miu.cse.heartlink.service.impl;

import edu.miu.cse.heartlink.dto.request.ConversationRequestDto;
import edu.miu.cse.heartlink.dto.request.MessageRequestDto;
import edu.miu.cse.heartlink.dto.response.ConversationResponseDto;
import edu.miu.cse.heartlink.mapper.ConversationMapper;
import edu.miu.cse.heartlink.model.Conversation;
import edu.miu.cse.heartlink.model.Item;
import edu.miu.cse.heartlink.model.Message;
import edu.miu.cse.heartlink.model.User;
import edu.miu.cse.heartlink.repository.ConversationRepository;
import edu.miu.cse.heartlink.repository.ItemRepository;
import edu.miu.cse.heartlink.repository.MessageRepository;
import edu.miu.cse.heartlink.repository.UserRepository;
import edu.miu.cse.heartlink.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ConversationMapper conversationMapper;

    @Override
    public Optional<ConversationResponseDto> createConversation(ConversationRequestDto conversationRequestDto) {
        // Check if conversation already exists
        Optional<Conversation> existingConversation = conversationRepository.findBySenderReceiverAndItem(
                conversationRequestDto.senderId(),
                conversationRequestDto.receiverId(),
                conversationRequestDto.itemId()
        );

        if (existingConversation.isPresent()) {
            // Optionally, return the existing conversation details
            Conversation conversation = existingConversation.get();
            return Optional.of(new ConversationResponseDto(
                    conversation.getTitle(),
                    conversation.getCreatedDateTime(),
                    conversation.getSender().getUserId(),
                    conversation.getReceiver().getUserId(),
                    conversation.getItem().getId()
            ));
        }

        // If no existing conversation, create a new one
        Conversation newConversation = new Conversation();
        newConversation.setTitle(conversationRequestDto.title());
        newConversation.setSender(userRepository.findById(conversationRequestDto.senderId()).orElseThrow(() -> new RuntimeException("Sender not found")));
        newConversation.setReceiver(userRepository.findById(conversationRequestDto.receiverId()).orElseThrow(() -> new RuntimeException("Receiver not found")));
        newConversation.setItem(itemRepository.findById(conversationRequestDto.itemId()).orElseThrow(() -> new RuntimeException("Item not found")));

        Conversation savedConversation = conversationRepository.save(newConversation);

        return Optional.of(new ConversationResponseDto(
                savedConversation.getTitle(),
                savedConversation.getCreatedDateTime(),
                savedConversation.getSender().getUserId(),
                savedConversation.getReceiver().getUserId(),
                savedConversation.getItem().getId()
        ));
    }

    @Override
    public Optional<ConversationResponseDto> findConversationByConversationId(Integer conversationId) {
        // Step 1: Retrieve the conversation by ID
        Optional<Conversation> conversationOpt = conversationRepository.findById(conversationId);

        // Step 2: If the conversation exists, map it to a response DTO
        if (conversationOpt.isPresent()) {
            Conversation conversation = conversationOpt.get();

            // Create the response DTO
            ConversationResponseDto responseDto = new ConversationResponseDto(
                    conversation.getTitle(),
                    conversation.getCreatedDateTime(),
                    conversation.getSender().getUserId(),
                    conversation.getReceiver().getUserId(),
                    conversation.getItem().getId()
            );

            return Optional.of(responseDto);
        }

        // Step 3: Return an empty Optional if the conversation is not found
        return Optional.empty();
    }
}
