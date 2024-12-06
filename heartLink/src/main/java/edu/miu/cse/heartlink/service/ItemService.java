package edu.miu.cse.heartlink.service;

import edu.miu.cse.heartlink.dto.request.ImageRequestDto;
import edu.miu.cse.heartlink.dto.request.ItemRequestDto;
import edu.miu.cse.heartlink.dto.request.UserRequestDto;
import edu.miu.cse.heartlink.dto.response.ImageResponseDto;
import edu.miu.cse.heartlink.dto.response.ItemResponseDto;
import edu.miu.cse.heartlink.dto.response.UserResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    //create Item
    Optional<ItemResponseDto> createItem(ItemRequestDto itemRequestDto);

    //upload Images
    List<ImageResponseDto> uploadImages(Integer itemId, List<MultipartFile> images);

    //update Images
    List<ImageResponseDto> updateImages(Integer itemId, List<MultipartFile> images);

    //update Item
    Optional<ItemResponseDto> updateItem(Integer itemId,ItemRequestDto itemRequestDto);

    //find Item
    Optional<ItemResponseDto> findById(Integer itemId);

    //find All Items by donar (depends on log In user)
    List<ItemResponseDto> findAllItemsByDonar(Integer userId);

    //delete Item before no item Request
    void deleteItem(Integer itemId);

}
