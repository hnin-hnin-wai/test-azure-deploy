package edu.miu.cse.heartlink.dto.response;
import java.time.LocalDateTime;


public record ConversationResponseDto(String title, LocalDateTime createdDateTime,Integer senderId, Integer receiverId,Integer itemId) {
}


