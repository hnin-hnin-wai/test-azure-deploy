package edu.miu.cse.heartlink.mapper;

import edu.miu.cse.heartlink.dto.request.ConversationRequestDto;
import edu.miu.cse.heartlink.dto.response.ConversationResponseDto;
import edu.miu.cse.heartlink.model.Conversation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ConversationMapper {

//    ConversationMapper conversationMapper= Mappers.getMapper(ConversationMapper.class);
////(String title, LocalDateTime createdDateTime, Integer senderId, Integer receiverId,Integer itemId
//    @Mapping(source = "senderId",target = "sender.userId")
//    @Mapping(source = "receiverId",target = "receiver.userId")
//    @Mapping(source = "itemId",target = "item.id")
//    Conversation ConversationRequestDtoToConversation(ConversationRequestDto conversationRequestDto);
//
//    @Mapping(source = "sender.userId",target = "senderId")
//    @Mapping(source = "receiver.userId",target = "receiverId")
//    @Mapping(source = "item.id",target = "itemId")
//    ConversationResponseDto ConversationToConversationResponseDto(Conversation conversation);
}
