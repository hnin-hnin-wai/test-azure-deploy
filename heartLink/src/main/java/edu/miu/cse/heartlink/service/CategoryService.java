package edu.miu.cse.heartlink.service;

import edu.miu.cse.heartlink.dto.request.CategoryRequestDto;
import edu.miu.cse.heartlink.dto.response.CategoryResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CategoryService {
    //create
    Optional<CategoryResponseDto> createCategory(CategoryRequestDto categoryRequestDto);
    //findbyname
    Optional<CategoryResponseDto> findByName(String categoryName);
    //findAll
    List<CategoryResponseDto> findAllCategories();
    //update
    Optional<CategoryResponseDto> updateCategory(String name,CategoryRequestDto categoryRequestDto);
    //delete
    void deleteCategory(String name);
}
