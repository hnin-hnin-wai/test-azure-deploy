package edu.miu.cse.heartlink.service;

import edu.miu.cse.heartlink.dto.response.ItemClaimTransactionResponseDto;

import java.util.List;
import java.util.Optional;

public interface ItemClaimTransactionService {

    Optional<ItemClaimTransactionResponseDto> createItemRequestTransaction(Integer itemRequestId);

    List<ItemClaimTransactionResponseDto> getAllItemRequestTransactionsByUserId(Integer userId);

}
