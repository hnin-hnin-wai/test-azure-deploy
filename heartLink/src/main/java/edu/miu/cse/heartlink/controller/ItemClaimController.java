package edu.miu.cse.heartlink.controller;


import edu.miu.cse.heartlink.dto.request.ItemClaimRequestDto;
import edu.miu.cse.heartlink.dto.response.ItemClaimResponseDto;
import edu.miu.cse.heartlink.dto.response.ItemResponseDto;
import edu.miu.cse.heartlink.service.ItemClaimService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/item-claims")
@RequiredArgsConstructor
public class ItemClaimController {

    private final ItemClaimService itemClaimService;

    @GetMapping
    public ResponseEntity<List<ItemClaimResponseDto>> findAllItemRequest() {
        List<ItemClaimResponseDto> itemRequestResponseDtos=itemClaimService.findAllItemRequest();
        return ResponseEntity.status(HttpStatus.OK).body(itemRequestResponseDtos);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<List<ItemClaimResponseDto>> findItemRequestById(@PathVariable Integer itemId) {
        List<ItemClaimResponseDto> itemRequestResponseDtos=itemClaimService.findItemRequestByItemId(itemId);
        return ResponseEntity.status(HttpStatus.OK).body(itemRequestResponseDtos);
    }

    @PostMapping
    public ResponseEntity<ItemClaimResponseDto> createItemRequest(@RequestBody ItemClaimRequestDto itemRequestRequestDto) {
        Optional<ItemClaimResponseDto> itemRequestResponseDto = itemClaimService.createItemRequest(itemRequestRequestDto);
        if(itemRequestResponseDto.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(itemRequestResponseDto.get());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @GetMapping("/receiver/{receiverId}")
    public ResponseEntity<List<ItemResponseDto>> getAcceptedItemsByReceiverId(@PathVariable Integer receiverId) {
        List<ItemResponseDto> itemResponseDtos = itemClaimService.findAllAcceptedItemsByReceiverId(receiverId);
        return ResponseEntity.status(HttpStatus.OK).body(itemResponseDtos);
    }

}
