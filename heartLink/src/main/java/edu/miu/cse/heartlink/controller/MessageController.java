package edu.miu.cse.heartlink.controller;

import edu.miu.cse.heartlink.dto.request.MessageRequestDto;
import edu.miu.cse.heartlink.dto.response.MessageResponseDto;
import edu.miu.cse.heartlink.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<Optional<MessageResponseDto>> addMessage(@RequestBody MessageRequestDto requestDto) {
        Optional<MessageResponseDto> responseDto = messageService.addMessage(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
    @GetMapping("/conversations/{conversationId}")
    public ResponseEntity<Page<MessageResponseDto>> getMessages(
            @PathVariable Integer conversationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<MessageResponseDto> messages = messageService.getMessagesByConversationId(conversationId, page, size);
        return ResponseEntity.ok(messages);
    }
}

