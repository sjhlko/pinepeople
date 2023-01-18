package com.lion.pinepeople.controller;

import com.lion.pinepeople.domain.dto.CategoryDTO;
import com.lion.pinepeople.domain.response.Response;
import com.lion.pinepeople.service.CategoryService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "카테고리")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public Response saveCategory (@RequestBody CategoryDTO categoryDTO) {
        categoryService.saveCategory(categoryDTO);
        return Response.success("카테고리 등록 성공");
    }

    @GetMapping("/{branch}")
    public Response getCategoryByBranch (@PathVariable String branch) {
        Map<String, CategoryDTO> response = categoryService.getCategoryByBranch(branch);
        return Response.success(response);
    }

    @PatchMapping("/{categoryId}")
    public Response updateCategory(@PathVariable Long categoryId,@RequestBody CategoryDTO categoryDTO) {
        categoryService.updateCategory(categoryId, categoryDTO);
        return Response.success("카테고리 수정 성공");
    }

    @DeleteMapping ("/{categoryId}")
    @ResponseBody
    public Response deleteCategory (@PathVariable Long categoryId){
        categoryService.deleteCategory(categoryId);
        return Response.success("카테고리 삭제 성공");
    }
}
