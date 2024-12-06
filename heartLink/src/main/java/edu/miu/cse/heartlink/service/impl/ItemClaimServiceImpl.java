package edu.miu.cse.heartlink.service.impl;

import edu.miu.cse.heartlink.constant.Status;
import edu.miu.cse.heartlink.dto.request.ItemClaimRequestDto;
import edu.miu.cse.heartlink.dto.response.CategoryResponseDto;
import edu.miu.cse.heartlink.dto.response.ImageResponseDto;
import edu.miu.cse.heartlink.dto.response.ItemClaimResponseDto;
import edu.miu.cse.heartlink.dto.response.ItemResponseDto;
import edu.miu.cse.heartlink.model.Item;
import edu.miu.cse.heartlink.model.ItemClaim;
import edu.miu.cse.heartlink.model.User;
import edu.miu.cse.heartlink.repository.ItemRepository;
import edu.miu.cse.heartlink.repository.ItemClaimRepository;
import edu.miu.cse.heartlink.repository.UserRepository;
import edu.miu.cse.heartlink.service.ItemClaimService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemClaimServiceImpl implements ItemClaimService {
    private final ItemClaimRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public Optional<ItemClaimResponseDto> createItemRequest(ItemClaimRequestDto requestDto) {
        // Fetch the associated item
        Optional<Item> itemOpt = itemRepository.findById(requestDto.itemId());
        if (itemOpt.isEmpty()) {
            throw new RuntimeException("Item not found with ID: " + requestDto.itemId());
        }

        // Fetch the donor
        Optional<User> donorOpt = userRepository.findById(requestDto.donarId());
        if (donorOpt.isEmpty()) {
            throw new RuntimeException("Donor not found with ID: " + requestDto.donarId());
        }

        // Fetch the receiver
        Optional<User> receiverOpt = userRepository.findById(requestDto.receiverId());
        if (receiverOpt.isEmpty()) {
            throw new RuntimeException("Receiver not found with ID: " + requestDto.receiverId());
        }

        // Create the ItemRequest entity
        ItemClaim itemRequest = new ItemClaim();
        itemRequest.setStatus(Status.valueOf(requestDto.status().toUpperCase()));
        itemRequest.setItem(itemOpt.get());
        itemRequest.setDonar(donorOpt.get());
        itemRequest.setReceiver(receiverOpt.get());

        // Save the item request
        ItemClaim savedItemRequest = itemRequestRepository.save(itemRequest);

        // Map the saved entity to a response DTO
        ItemClaimResponseDto responseDto = new ItemClaimResponseDto(

                savedItemRequest.getStatus().name(),
                savedItemRequest.getItem().getId(),
                savedItemRequest.getDonar().getUserId(),
                savedItemRequest.getReceiver().getUserId()
        );

        return Optional.of(responseDto);
    }


    @Override
    public List<ItemClaimResponseDto> findAllItemRequest() {
        List<ItemClaim> itemRequests = itemRequestRepository.findAll();

        return itemRequests.stream().map(itemRequest -> new ItemClaimResponseDto(
                itemRequest.getStatus().name(),
                itemRequest.getItem().getId(),
                itemRequest.getDonar().getUserId(),
                itemRequest.getReceiver().getUserId()
        )).toList();
    }

    @Override
    public List<ItemClaimResponseDto> findItemRequestByItemId(long itemId) {
        List<ItemClaim> itemRequests = itemRequestRepository.findByItemId(itemId);
        return itemRequests.stream().map(itemRequest -> new ItemClaimResponseDto(

                itemRequest.getStatus().name(),
                itemRequest.getItem().getId(),
                itemRequest.getDonar().getUserId(),
                itemRequest.getReceiver().getUserId()
        )).toList();
    }

    @Override
    public List<ItemResponseDto> findAllAcceptedItemsByReceiverId(Integer receiverId) {
        List<Item> items = itemRequestRepository.findAllAcceptedItemsByReceiverId(receiverId);

        return items.stream()
                .map(item -> new ItemResponseDto(
                        item.getName(),
                        new CategoryResponseDto(item.getId(), item.getName()),
                        item.getUser().getUserId()
                ))
                .toList();
    }

}
