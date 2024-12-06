package edu.miu.cse.heartlink.service;

import edu.miu.cse.heartlink.dto.request.MessageRequestDto;
import edu.miu.cse.heartlink.dto.response.MessageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MessageService {
    Optional<MessageResponseDto> addMessage(MessageRequestDto messageRequestDto);
    Page<MessageResponseDto> getMessagesByConversationId(Integer conversationId,int page, int size);
}
