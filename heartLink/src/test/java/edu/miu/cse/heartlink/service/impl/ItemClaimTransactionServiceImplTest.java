package edu.miu.cse.heartlink.service.impl;

import edu.miu.cse.heartlink.constant.Status;
import edu.miu.cse.heartlink.dto.response.ItemClaimTransactionResponseDto;
import edu.miu.cse.heartlink.model.Item;
import edu.miu.cse.heartlink.model.ItemClaim;
import edu.miu.cse.heartlink.model.ItemClaimTransaction;
import edu.miu.cse.heartlink.model.User;
import edu.miu.cse.heartlink.repository.ItemClaimRepository;
import edu.miu.cse.heartlink.repository.ItemClaimTransactionRepository;
import edu.miu.cse.heartlink.repository.UserRepository;
import edu.miu.cse.heartlink.service.ItemClaimTransactionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ItemClaimTransactionServiceImplTest {

    @Mock
    private ItemClaimTransactionRepository itemClaimTransactionRepository;

    @Mock
    private ItemClaimRepository itemClaimRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ItemClaimTransactionServiceImpl itemClaimTransactionServiceImpl;

    @Test
    void createItemRequestTransaction() {
        // Arrange
        Integer itemRequestId = 1;

        Item item = new Item();
        item.setId(101);

        User donor = new User();
        donor.setUserId(201);

        User receiver = new User();
        receiver.setUserId(301);

        ItemClaim itemRequest = new ItemClaim();
        itemRequest.setId(itemRequestId);
        itemRequest.setItem(item);
        itemRequest.setDonar(donor);
        itemRequest.setReceiver(receiver);
        itemRequest.setStatus(Status.CREATED);

        // Mock the repository behavior
        Mockito.when(itemClaimRepository.findById(itemRequestId)).thenReturn(Optional.of(itemRequest));
        Mockito.when(itemClaimRepository.save(Mockito.any(ItemClaim.class))).thenReturn(itemRequest);

        ItemClaimTransaction transaction = new ItemClaimTransaction();
        transaction.setId(1);
        transaction.setItemRequest(itemRequest);

        Mockito.when(itemClaimTransactionRepository.save(Mockito.any(ItemClaimTransaction.class))).thenReturn(transaction);

        // Act
        Optional<ItemClaimTransactionResponseDto> result = itemClaimTransactionServiceImpl.createItemRequestTransaction(itemRequestId);

        // Assert
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(Status.ACCEPTED.name(), result.get().itemRequestResponseDto().status());
        Assertions.assertEquals(101, result.get().itemRequestResponseDto().itemId());
        Assertions.assertEquals(201, result.get().itemRequestResponseDto().donarId());
        Assertions.assertEquals(301, result.get().itemRequestResponseDto().receiverId());

        Mockito.verify(itemClaimRepository).save(itemRequest);
        Mockito.verify(itemClaimTransactionRepository).save(Mockito.any(ItemClaimTransaction.class));
    }


    @Test
    void getAllItemRequestTransactionsByUserId() {
        // Arrange
        Integer userId = 301;

        Item item = new Item();
        item.setId(101);

        User donor = new User();
        donor.setUserId(201);

        User receiver = new User();
        receiver.setUserId(userId);

        ItemClaim itemRequest = new ItemClaim();
        itemRequest.setId(1);
        itemRequest.setItem(item);
        itemRequest.setDonar(donor);
        itemRequest.setReceiver(receiver);
        itemRequest.setStatus(Status.ACCEPTED);

        ItemClaimTransaction transaction = new ItemClaimTransaction();
        transaction.setId(1);
        transaction.setItemRequest(itemRequest);

        Mockito.when(itemClaimTransactionRepository.findAllByUserId(userId)).thenReturn(List.of(transaction));

        // Act
        List<ItemClaimTransactionResponseDto> result = itemClaimTransactionServiceImpl.getAllItemRequestTransactionsByUserId(userId);

        // Assert
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(Status.ACCEPTED.name(), result.get(0).itemRequestResponseDto().status());
        Assertions.assertEquals(101, result.get(0).itemRequestResponseDto().itemId());
        Assertions.assertEquals(201, result.get(0).itemRequestResponseDto().donarId());
        Assertions.assertEquals(userId, result.get(0).itemRequestResponseDto().receiverId());

        Mockito.verify(itemClaimTransactionRepository).findAllByUserId(userId);
    }

}