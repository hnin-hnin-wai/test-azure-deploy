package edu.miu.cse.heartlink.controller;

import edu.miu.cse.heartlink.dto.request.CategoryRequestDto;
import edu.miu.cse.heartlink.dto.response.CategoryResponseDto;
import edu.miu.cse.heartlink.model.Category;
import edu.miu.cse.heartlink.repository.CategoryRepository;
import edu.miu.cse.heartlink.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDto> CreateCategory(
            @Valid
            @RequestBody
            CategoryRequestDto categoryRequestDto
    ){
        Optional<CategoryResponseDto> categoryResponseDto = categoryService.createCategory(categoryRequestDto);

        if(categoryResponseDto.isPresent()){
            System.out.println("category response::"+categoryResponseDto.get());
            return ResponseEntity.status(HttpStatus.CREATED).body(categoryResponseDto.get());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @GetMapping("/{name}")
    public ResponseEntity<CategoryResponseDto> findByCategoryName(@PathVariable String name){
        Optional<CategoryResponseDto> categoryResponseDto=categoryService.findByName(name);
            return ResponseEntity.status(HttpStatus.OK).body(categoryResponseDto.get());
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> findAllCategories(){
        List<CategoryResponseDto> categoryResponseDtos=categoryService.findAllCategories();
        return ResponseEntity.status(HttpStatus.OK).body(categoryResponseDtos);
    }

    @PutMapping("/{name}")
    public ResponseEntity<CategoryResponseDto> UpdateCategory(
            @PathVariable String name,
            @RequestBody @Valid CategoryRequestDto categoryRequestDto
    ){
        Optional<CategoryResponseDto> categoryResponseDto=categoryService.updateCategory(name, categoryRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(categoryResponseDto.get());
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<CategoryResponseDto> deleteCategory(@PathVariable String name){
        categoryService.deleteCategory(name);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
