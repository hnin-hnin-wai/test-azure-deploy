package edu.miu.cse.heartlink.service;

import edu.miu.cse.heartlink.dto.request.ConversationRequestDto;
import edu.miu.cse.heartlink.dto.response.ConversationResponseDto;
import edu.miu.cse.heartlink.model.Conversation;

import java.util.Optional;

public interface ConversationService {

    //create conversation
    Optional<ConversationResponseDto> createConversation(ConversationRequestDto conversationRequestDto);

    //get conversation
    Optional<ConversationResponseDto> findConversationByConversationId (Integer conversationId);


}
