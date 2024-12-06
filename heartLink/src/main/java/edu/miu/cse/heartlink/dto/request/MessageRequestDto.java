package edu.miu.cse.heartlink.dto.request;

public record MessageRequestDto(String content, Integer conversationId, Integer senderId, Integer receiverId) {
}
