package com.lion.pinepeople.service;

import com.lion.pinepeople.domain.dto.category.CategoryDto;
import com.lion.pinepeople.domain.entity.Category;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public final String ROOT = "ROOT";

    /**
     * 카테고리 생성 메서드
     * 처음에 code로 ROOT 생성 그후 자식 카테고리 생성 시에는 parentCategoryName에
     * 부모의 branch 명을 적고 카테고리 생성
     * **/
    public Long saveCategory(CategoryDto categoryDTO) {
        Category category = categoryDTO.toEntity();
        //대분류 등록
        if (categoryDTO.getParentCategoryName() == null) {
            //JPA 사용하여 DB에서 branch와 name의 중복값을 검사. (대분류에서만 가능)
            if (categoryRepository.existsByBranchAndName(categoryDTO.getBranch(), categoryDTO.getName())) {
                throw new AppException(ErrorCode.CATEGORY_NAME_FALUT,"branch와 name이 같을 수 없습니다.");
            }
            //orElse로 refactor
            Category rootCategory = categoryRepository.findByBranchAndName(categoryDTO.getBranch(), ROOT)
                    .orElseGet(() ->
                            Category.builder()
                                    .name("ROOT")
                                    .code("ROOT")
                                    .branch(categoryDTO.getBranch())
                                    .level(0)
                                    .build()
                    );
            if (!categoryRepository.existsByBranchAndName(categoryDTO.getBranch(), ROOT)) {
                categoryRepository.save(rootCategory);
            }

            /*자식카테고리 생성 시 ROOT 설정 and level에 값 넣어주기*/
            category.addParentCategory(rootCategory);
            category.addLevel(1);
            //중, 소분류 등록
        } else {
            String parentCategoryName = categoryDTO.getParentCategoryName();
            Category parentCategory = categoryRepository.findByBranchAndName(categoryDTO.getBranch(), parentCategoryName)
                    .orElseThrow(() -> new IllegalArgumentException("부모 카테고리 없음 예외"));
            /*category.setLevel(parentCategory.getLevel() + 1);
            category.setParentCategory(parentCategory);*/
            category.addLevel(parentCategory.getLevel()+1);
            category.addParentCategory(parentCategory);
            parentCategory.getSubCategory().add(category);
        }
        return categoryRepository.save(category).getId();
    }

    /**
     * 카테고리 조회 메서드
     * 대분류 카테고리와 자식 카테고리를 같이 조회하기 위해 map으로 반환
     * **/
    public Map<String, CategoryDto> getCategoryByBranch(String branch) {
        /*우선 대분류 카테고리가 존재하는지 check*/
        Category category = categoryRepository.findByBranchAndCode(branch, ROOT)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND,"해당 카테고리를 찾을 수 없습니다."));

        /*엔티티 responseDTO로 변환*/
        CategoryDto categoryDTO = new CategoryDto(category);
        /*MAP구조로 저장*/
        Map<String, CategoryDto> data = new HashMap<>();
        data.put(categoryDTO.getName(), categoryDTO);
        return data;
    }

    /**
     * @param branch 카테고리의 branch 로 해당 branch에 속하는 카테고리들 리스트 조회
     * **/
    public List<CategoryDto> getCategoryByBranchList(String branch) {
        List<Category> categoryList = categoryRepository.findByBranch(branch);
        List<CategoryDto> categoryDtoList
                = categoryList.stream().map(c -> new CategoryDto(c)).collect(Collectors.toList());
        return categoryDtoList;
    }

    /**
     * 하위 카테고리가 없을 경우 repository에서도 삭제
     * 하위 카테고리가 있을 경우에는 name을 "Deleted category" 로 변경만 할것
     * **/
    public void deleteCategory(Long categoryId) {
        Category category = findCategory(categoryId);
        if (category.getSubCategory().size() == 0) { //하위 카테고리 없을 경우
            Category parentCategory = findCategory(category.getParentCategory().getId());
            if (!parentCategory.getName().equals("ROOT")) { // ROOT가 아닌 다른 부모가 있을 경우
                parentCategory.getSubCategory().remove(category);
            }
            categoryRepository.deleteById(category.getId());
        } else { //하위 카테고리 있을 경우
            Category parentCategory = findCategory(category.getParentCategory().getId());
            //ROOT아닌 부모가 있을 경우
            if (!parentCategory.getName().equals("ROOT")) {
                parentCategory.getSubCategory().remove(category);
            }
            category.changeCategoryName("Deleted category");
            /*category.setName("Deleted category");*/
        }
    }


    /**카테고리의 name만 바꿔주는 메서드**/
    public Long updateCategory (Long categoryId, CategoryDto categoryDTO) {
        Category category = findCategory(categoryId);
        category.changeCategoryName(categoryDTO.getName());
       /* category.setName(categoryDTO.getName());*/
        return category.getId();
    }

    /**카테고리 조회**/
    private Category findCategory(Long categoryId) {
       return  categoryRepository.findById(categoryId).orElseThrow(()->
               new AppException(ErrorCode.CATEGORY_NOT_FOUND,categoryId +"번의 카테고리를 찾을 수 없습니다."));
    }



}