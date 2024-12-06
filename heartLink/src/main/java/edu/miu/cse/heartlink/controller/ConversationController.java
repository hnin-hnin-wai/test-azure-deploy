package edu.miu.cse.heartlink.controller;

import edu.miu.cse.heartlink.dto.request.ConversationRequestDto;
import edu.miu.cse.heartlink.dto.request.UserRequestDto;
import edu.miu.cse.heartlink.dto.response.ConversationResponseDto;
import edu.miu.cse.heartlink.dto.response.UserResponseDto;
import edu.miu.cse.heartlink.service.ConversationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;

    @PostMapping
    public ResponseEntity<ConversationResponseDto> CreateConversation(
            @Valid
            @RequestBody
            ConversationRequestDto conversationRequestDto
    ) {

        Optional<ConversationResponseDto> conversationResponseDto = conversationService.createConversation(conversationRequestDto);
        if (conversationResponseDto.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(conversationResponseDto.get());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    //find conversation ById
    @GetMapping("/{conversationId}")
    public ResponseEntity<ConversationResponseDto> findByConversationId(@PathVariable Integer conversationId) {
        Optional<ConversationResponseDto> conversationResponseDto = conversationService.findConversationByConversationId(conversationId);
        return ResponseEntity.status(HttpStatus.OK).body(conversationResponseDto.get());
    }

}
