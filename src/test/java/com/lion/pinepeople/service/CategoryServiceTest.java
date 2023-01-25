package com.lion.pinepeople.service;

import com.lion.pinepeople.domain.dto.category.CategoryDto;
import com.lion.pinepeople.domain.entity.Category;
import com.lion.pinepeople.repository.CategoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CategoryServiceTest {
    @InjectMocks
    CategoryService categoryService;
    @Mock
    CategoryRepository categoryRepository;



    private CategoryDto createCategoryDTO(String testBranch, String testCode, String testName) {
        CategoryDto categoryDTO = new CategoryDto();
        categoryDTO.setBranch(testBranch);
        categoryDTO.setCode(testCode);
        categoryDTO.setName(testName);
        return categoryDTO;
    }

    //Find Category
    private Category findCategory (Long savedId) {
        return categoryRepository.findById(savedId).orElseThrow(IllegalArgumentException::new);
    }

    @Test
    public void 카테고리_저장_테스트 () {
        //given
        CategoryDto categoryDTO = createCategoryDTO("TestBranch", "TestCode", "TestName");
        Long savedId = categoryService.saveCategory(categoryDTO);
        //when
        Category category = findCategory(savedId);
        //then
       Assertions.assertThat(category.getCode()).isEqualTo("TestCode");
    }
    @Test
    public void 카테고리_업데이트_테스트 () {
        //given
        CategoryDto categoryDTO = createCategoryDTO("TestBranch", "TestCode", "TestName");
        Long savedId = categoryService.saveCategory(categoryDTO);

        Category category = findCategory(savedId);
        CategoryDto targetCategory = new CategoryDto(category);
        targetCategory.setName("UpdateCategory");

        //when
       /* new CategoryDto();

        Long updateId = categoryService.updateCategory("TestBranch", "TestCode", targetCategory);
        Category updatedCategory = findCategory(updateId);
*/
        //then
       // Assertions.assertThat(updatedCategory.getName()).isEqualTo("UpdateCategory");
    }

}