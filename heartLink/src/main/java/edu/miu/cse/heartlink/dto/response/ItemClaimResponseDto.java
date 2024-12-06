package edu.miu.cse.heartlink.dto.response;

public record ItemClaimResponseDto(
        String status,
        Integer itemId,
        Integer donarId,
        Integer receiverId
) {
}
