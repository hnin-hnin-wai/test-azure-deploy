package edu.miu.cse.heartlink.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ConversationRequestDto(
        @NotBlank String title,
        @NotNull LocalDateTime createdDateTime,
        @NotNull Integer senderId,
        @NotNull Integer receiverId,
        @NotNull Integer itemId
) {
}
