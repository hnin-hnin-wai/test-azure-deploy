package edu.miu.cse.heartlink.service;

import edu.miu.cse.heartlink.dto.request.ItemClaimRequestDto;
import edu.miu.cse.heartlink.dto.response.ItemClaimResponseDto;
import edu.miu.cse.heartlink.dto.response.ItemResponseDto;

import java.util.List;
import java.util.Optional;

public interface ItemClaimService {

    Optional<ItemClaimResponseDto> createItemRequest(ItemClaimRequestDto requestDto);

    List<ItemClaimResponseDto> findAllItemRequest();

    List<ItemClaimResponseDto> findItemRequestByItemId(long itemId);

    List<ItemResponseDto> findAllAcceptedItemsByReceiverId(Integer receiverId);
}
