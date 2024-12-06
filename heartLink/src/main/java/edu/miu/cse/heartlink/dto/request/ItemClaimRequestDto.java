package edu.miu.cse.heartlink.dto.request;

public record ItemClaimRequestDto(
        String status,
        Integer itemId,
        Integer donarId,
        Integer receiverId

) {
}

