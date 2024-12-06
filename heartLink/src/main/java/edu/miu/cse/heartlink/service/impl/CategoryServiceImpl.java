package edu.miu.cse.heartlink.service.impl;

import edu.miu.cse.heartlink.dto.request.CategoryRequestDto;
import edu.miu.cse.heartlink.dto.response.CategoryResponseDto;
import edu.miu.cse.heartlink.exception.category.CategoryNotFoundException;
import edu.miu.cse.heartlink.mapper.CategoryMapper;
import edu.miu.cse.heartlink.model.Category;
import edu.miu.cse.heartlink.repository.CategoryRepository;
import edu.miu.cse.heartlink.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
   // private final CategoryMapper categoryMapper;

    @Override
    public Optional<CategoryResponseDto> createCategory(CategoryRequestDto categoryRequestDto) {
        Category category = new Category();
        category.setName(categoryRequestDto.name());
        Category savedCategory=categoryRepository.save(category);
        CategoryResponseDto categoryResponseDto=new CategoryResponseDto(savedCategory.getId(),savedCategory.getName());
        return Optional.of(categoryResponseDto);
        //Category savedCategory=categoryRepository.save(CategoryMapper.INSTANCE.categoryRequestDtoToCategory(categoryRequestDto));
       // return Optional.of(CategoryMapper.INSTANCE.categoryToCategoryResponseDto(savedCategory));
    }

    @Override
    public Optional<CategoryResponseDto> findByName(String categoryName) {
        Optional<Category> foundCategory=categoryRepository.findByName(categoryName);
        if(foundCategory.isPresent()){
            CategoryResponseDto categoryResponseDto=new CategoryResponseDto(foundCategory.get().getId(),categoryName);
            return Optional.of(categoryResponseDto);
        }
        throw new CategoryNotFoundException(categoryName+ " is not found.");
    }

    @Override
    public List<CategoryResponseDto> findAllCategories() {
        List<Category> categories=categoryRepository.findAll();
        List<CategoryResponseDto> categoryResponseDtos=new ArrayList<>();
        for(Category category:categories){
            categoryResponseDtos.add(new CategoryResponseDto(category.getId(),category.getName()));
        }

        return categoryResponseDtos;
    }

    @Override
    public Optional<CategoryResponseDto> updateCategory(String name, CategoryRequestDto categoryRequestDto) {
       Optional<Category> foundCategory =categoryRepository.findByName(name);
        if(foundCategory.isPresent()){
            Category category = foundCategory.get();
            category.setName(categoryRequestDto.name());

            Category savedCategory=categoryRepository.save(category);
            return Optional.of(new CategoryResponseDto(category.getId(),savedCategory.getName()));
        }
        throw new CategoryNotFoundException(name+ " is not found.");
    }

    @Override
    @Transactional
    public void deleteCategory(String name) {
        findByName(name).ifPresent(categoryName->{
            categoryRepository.deleteByName(name);
        });
    }
}
