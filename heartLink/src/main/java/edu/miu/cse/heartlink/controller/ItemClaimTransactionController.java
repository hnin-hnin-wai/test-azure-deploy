package edu.miu.cse.heartlink.controller;

import edu.miu.cse.heartlink.dto.request.ItemClaimTransactionRequestDto;
import edu.miu.cse.heartlink.dto.response.ItemClaimTransactionResponseDto;
import edu.miu.cse.heartlink.service.ItemClaimTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/item-transactions")
@RequiredArgsConstructor
public class ItemClaimTransactionController {

    private final ItemClaimTransactionService itemRequestTransactionService;

    @PostMapping
    ResponseEntity<ItemClaimTransactionResponseDto> createItemRequestTransaction(
            @RequestBody
            ItemClaimTransactionRequestDto itemRequestTransactionRequestDto
    ) {
        Optional<ItemClaimTransactionResponseDto> itemRequestTransactionResponseDto = itemRequestTransactionService.createItemRequestTransaction(itemRequestTransactionRequestDto.itemRequestId());
        if (itemRequestTransactionResponseDto.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(itemRequestTransactionResponseDto.get());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @GetMapping("/{userId}")
    ResponseEntity<List<ItemClaimTransactionResponseDto>> getItemRequestTransaction(@PathVariable Integer userId) {

        List<ItemClaimTransactionResponseDto> itemRequestTransactionResponseDtos=itemRequestTransactionService.getAllItemRequestTransactionsByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(itemRequestTransactionResponseDtos);
    }

}