package com.lion.pinepeople.controller;

import com.lion.pinepeople.domain.dto.category.CategoryDto;
import com.lion.pinepeople.domain.response.Response;
import com.lion.pinepeople.service.CategoryService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = "카테고리")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    /**
     * 카테고리 대분류 / 소분류 등록
     * @param categoryDTO 카테고리 등록 dto request
     *
     * @return 카테고리 등록 성공 메세지
     */
    @PostMapping
    public Response saveCategory (@RequestBody CategoryDto categoryDTO) {
        categoryService.saveCategory(categoryDTO);
        return Response.success("카테고리 등록 성공");
    }

    /**
     * @param branch 카테고리의 branch 별로 페이징 조회
     *
     * @return map구조로 카테고리 페이징 응답
     */
    @GetMapping("/{branch}")
    public Response getCategoryByBranch (@PathVariable String branch) {
        Map<String, CategoryDto> response = categoryService.getCategoryByBranch(branch);
        return Response.success(response);
    }

    /**
     * @param categoryId categoryId로 카테테     *
     * @return 수정 성공 메세지
     */
    @PatchMapping("/{categoryId}")
    public Response updateCategory(@PathVariable Long categoryId,@RequestBody CategoryDto categoryDTO) {
        categoryService.updateCategory(categoryId, categoryDTO);
        return Response.success("카테고리 수정 성공");
    }

    /**
     * @param categoryId categoryId로 카테테     *
     * @return 삭제 성공 메세지
     */
    @DeleteMapping ("/{categoryId}")
    @ResponseBody
    public Response deleteCategory (@PathVariable Long categoryId){
        categoryService.deleteCategory(categoryId);
        return Response.success("카테고리 삭제 성공");
    }

    /**카테고리 단건 조회
     * @param branch 대부류 카테고리 조회
     * @param code 소분류 카테고리 조회
     * **/
    @GetMapping("/{branch}/{code}")
    public Response getCategory (@PathVariable String branch,@PathVariable String code) {
        return Response.success(categoryService.getCategoryDTO(branch,code));
    }
}
