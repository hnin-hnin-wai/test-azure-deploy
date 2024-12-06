package edu.miu.cse.heartlink.service.impl;

import edu.miu.cse.heartlink.constant.Status;
import edu.miu.cse.heartlink.dto.response.ItemClaimResponseDto;
import edu.miu.cse.heartlink.dto.response.ItemClaimTransactionResponseDto;
import edu.miu.cse.heartlink.model.ItemClaim;
import edu.miu.cse.heartlink.model.ItemClaimTransaction;
import edu.miu.cse.heartlink.repository.ItemClaimRepository;
import edu.miu.cse.heartlink.repository.ItemClaimTransactionRepository;
import edu.miu.cse.heartlink.repository.UserRepository;
import edu.miu.cse.heartlink.service.ItemClaimTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemClaimTransactionServiceImpl implements ItemClaimTransactionService {

    private final ItemClaimTransactionRepository itemRequestTransactionRepository;
    private final ItemClaimRepository itemRequestRepository;
    private final UserRepository userRepository;

    @Override
    public Optional<ItemClaimTransactionResponseDto> createItemRequestTransaction(Integer itemRequestId) {
        // Fetch the item request by ID from the DTO
        Optional<ItemClaim> itemRequestOpt = itemRequestRepository.findById(itemRequestId);
        if (itemRequestOpt.isEmpty()) {
            throw new RuntimeException("Item Request not found with ID: " + itemRequestId);
        }

        ItemClaim itemRequest = itemRequestOpt.get();
        // Update the status to ACCEPTED
        itemRequest.setStatus(Status.ACCEPTED);
        itemRequestRepository.save(itemRequest);

        // Create the ItemRequestTransaction entity
        ItemClaimTransaction transaction = new ItemClaimTransaction();
        transaction.setItemRequest(itemRequest);

        // Save the transaction
        ItemClaimTransaction savedTransaction = itemRequestTransactionRepository.save(transaction);

        // Map to response DTO
        ItemClaimResponseDto itemRequestResponseDto = new ItemClaimResponseDto(

                itemRequest.getStatus().name(),
                itemRequest.getItem().getId(),
                itemRequest.getDonar().getUserId(),
                itemRequest.getReceiver().getUserId()
        );

        ItemClaimTransactionResponseDto responseDto = new ItemClaimTransactionResponseDto(itemRequestResponseDto);

        return Optional.of(responseDto);
    }


    @Override
    public List<ItemClaimTransactionResponseDto> getAllItemRequestTransactionsByUserId(Integer userId) {
        // Fetch all item requests for the given userId
        List<ItemClaimTransaction> transactions = itemRequestTransactionRepository.findAllByUserId(userId);

        // Map transactions to response DTOs
        return transactions.stream().map(transaction -> {
            ItemClaim itemRequest = transaction.getItemRequest();
            ItemClaimResponseDto itemRequestResponseDto = new ItemClaimResponseDto(

                    itemRequest.getStatus().name(),
                    itemRequest.getItem().getId(),
                    itemRequest.getDonar().getUserId(),
                    itemRequest.getReceiver().getUserId()
            );
            return new ItemClaimTransactionResponseDto(itemRequestResponseDto);
        }).toList();
    }
}
