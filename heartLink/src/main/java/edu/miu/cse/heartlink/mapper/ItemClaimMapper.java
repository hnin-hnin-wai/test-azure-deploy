package edu.miu.cse.heartlink.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ItemClaimMapper {

    ItemClaimMapper itemRequestMapper= Mappers.getMapper(ItemClaimMapper.class);
//
//    @Mapping(source="userId",target = "donar.userId")
//    @Mapping(source="userId",target ="receiver.userId")
//    ItemRequest itemRequestRequestDtoToItemRequest(ItemRequestDto itemRequestDto);
//
//    @Mapping(source="donar.userId",target = "userId")
//    @Mapping(source = "receiver.userId",target = "receiverId")
//    ItemRequestResponseDto itemRequesttoItemRequestResponseDto(ItemRequest itemRequest);
}
