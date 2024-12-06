package edu.miu.cse.heartlink.dto.response;

import java.time.LocalDateTime;

public record MessageResponseDto(Integer messageId, String content, LocalDateTime createdDateTime, Integer conversationId, Integer senderId, Integer receiverId) {
}

