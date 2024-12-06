package edu.miu.cse.heartlink.dto.response;

import edu.miu.cse.heartlink.dto.request.CategoryRequestDto;
import jakarta.persistence.metamodel.IdentifiableType;

import java.util.List;

public record ItemResponseDto(String name,
                              CategoryResponseDto categoryResponseDto,
                              Integer userId
) {
}
