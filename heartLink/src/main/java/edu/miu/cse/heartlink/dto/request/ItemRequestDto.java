package edu.miu.cse.heartlink.dto.request;

import edu.miu.cse.heartlink.model.Image;

import java.util.List;

public record ItemRequestDto(
        String name,
        CategoryRequestDto categoryRequestDto,
        Integer userId
) {
}
