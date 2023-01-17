package com.lion.pinepeople.controller;

import com.lion.pinepeople.domain.dto.CategoryDTO;
import com.lion.pinepeople.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/categories")
    public Long saveCategory (@RequestBody CategoryDTO categoryDTO) {
        return categoryService.saveCategory(categoryDTO);
    }

    @GetMapping("/categories/{branch}")
    public Map<String, CategoryDTO> getCategoryByBranch (@PathVariable String branch) {
        return categoryService.getCategoryByBranch(branch);
    }

    @PutMapping("/categories/{id}/")
    public Long updateCategory(@PathVariable Long id, CategoryDTO categoryDTO) {
        return categoryService.updateCategory(id, categoryDTO);
    }
}
