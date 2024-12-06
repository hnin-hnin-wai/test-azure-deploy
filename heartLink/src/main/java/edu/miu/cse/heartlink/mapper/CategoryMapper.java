package edu.miu.cse.heartlink.mapper;

import edu.miu.cse.heartlink.dto.request.CategoryRequestDto;
import edu.miu.cse.heartlink.dto.response.CategoryResponseDto;
import edu.miu.cse.heartlink.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    Category categoryRequestDtoToCategory(CategoryRequestDto categoryRequestDto);

    CategoryResponseDto categoryToCategoryResponseDto(Category category);

}
