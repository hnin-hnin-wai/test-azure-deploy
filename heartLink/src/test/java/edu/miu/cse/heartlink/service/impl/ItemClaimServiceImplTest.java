package edu.miu.cse.heartlink.service.impl;

import edu.miu.cse.heartlink.constant.Status;
import edu.miu.cse.heartlink.dto.request.ItemClaimRequestDto;
import edu.miu.cse.heartlink.dto.response.ItemClaimResponseDto;
import edu.miu.cse.heartlink.dto.response.ItemResponseDto;
import edu.miu.cse.heartlink.model.Category;
import edu.miu.cse.heartlink.model.Item;
import edu.miu.cse.heartlink.model.ItemClaim;
import edu.miu.cse.heartlink.model.User;
import edu.miu.cse.heartlink.repository.ItemClaimRepository;
import edu.miu.cse.heartlink.repository.ItemRepository;
import edu.miu.cse.heartlink.repository.UserRepository;
import edu.miu.cse.heartlink.service.ItemClaimService;
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
class ItemClaimServiceImplTest {

    @Mock
    private ItemClaimRepository itemClaimRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ItemClaimServiceImpl itemClaimServiceImpl;

    @Test
    void createItemRequest() {
        // Arrange
        ItemClaimRequestDto requestDto = new ItemClaimRequestDto("CREATED", 1, 2, 3);
        Item item = new Item();
        item.setId(1);
        User donor = new User();
        donor.setUserId(2);
        User receiver = new User();
        receiver.setUserId(3);

        ItemClaim itemClaim = new ItemClaim();
        itemClaim.setStatus(Status.CREATED); // Set the status here
        itemClaim.setItem(item);
        itemClaim.setDonar(donor);
        itemClaim.setReceiver(receiver);

        Mockito.when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        Mockito.when(userRepository.findById(2)).thenReturn(Optional.of(donor));
        Mockito.when(userRepository.findById(3)).thenReturn(Optional.of(receiver));
        Mockito.when(itemClaimRepository.save(Mockito.any(ItemClaim.class))).thenReturn(itemClaim);

        // Act
        Optional<ItemClaimResponseDto> response = itemClaimServiceImpl.createItemRequest(requestDto);

        // Assert
        Assertions.assertTrue(response.isPresent());
        Assertions.assertEquals("CREATED", response.get().status());
        Assertions.assertEquals(1, response.get().itemId());
        Assertions.assertEquals(2, response.get().donarId());
        Assertions.assertEquals(3, response.get().receiverId());
  }

    @Test
    void findAllItemRequest() {
        // Arrange
        ItemClaim itemClaim = new ItemClaim();
        itemClaim.setStatus(Status.ACCEPTED);
        itemClaim.setItem(new Item() {{ setId(1); }});
        itemClaim.setDonar(new User() {{ setUserId(2); }});
        itemClaim.setReceiver(new User() {{ setUserId(3); }});

        Mockito.when(itemClaimRepository.findAll()).thenReturn(List.of(itemClaim));

        // Act
        List<ItemClaimResponseDto> responses = itemClaimServiceImpl.findAllItemRequest();

        // Assert
        Assertions.assertFalse(responses.isEmpty());
        Assertions.assertEquals(1, responses.get(0).itemId());
        Assertions.assertEquals(2, responses.get(0).donarId());
        Assertions.assertEquals(3, responses.get(0).receiverId());
    }

    @Test
    void findItemRequestByItemId() {
        // Arrange
        ItemClaim itemClaim = new ItemClaim();
        itemClaim.setStatus(Status.REJECTED);
        itemClaim.setItem(new Item() {{ setId(1); }});
        itemClaim.setDonar(new User() {{ setUserId(2); }});
        itemClaim.setReceiver(new User() {{ setUserId(3); }});

        Mockito.when(itemClaimRepository.findByItemId(1L)).thenReturn(List.of(itemClaim));

        // Act
        List<ItemClaimResponseDto> responses = itemClaimServiceImpl.findItemRequestByItemId(1L);

        // Assert
        Assertions.assertFalse(responses.isEmpty());
        Assertions.assertEquals("REJECTED", responses.get(0).status());
        Assertions.assertEquals(1, responses.get(0).itemId());
    }

    @Test
    void findAllAcceptedItemsByReceiverId() {
        // Arrange
        Item item = new Item();
        item.setName("Item1");
        item.setUser(new User() {{ setUserId(3); }});
        item.setCategory(new Category() {{ setId(1); setName("Category1"); }});

        Mockito.when(itemClaimRepository.findAllAcceptedItemsByReceiverId(3)).thenReturn(List.of(item));

        // Act
        List<ItemResponseDto> responses = itemClaimServiceImpl.findAllAcceptedItemsByReceiverId(3);

        // Assert
        Assertions.assertFalse(responses.isEmpty());
        Assertions.assertEquals("Item1", responses.get(0).name());
        Assertions.assertEquals(3, responses.get(0).userId());
    }

}