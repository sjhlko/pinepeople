package com.lion.pinepeople.domain.dto;

import com.lion.pinepeople.domain.entity.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDTO {

    private Long categoryId;
    private String branch;
    private String code;
    private String name;
    private String parentCategoryName;
    private Integer level;
    private Map<String, CategoryDTO> children;

    public CategoryDTO (Category entity) {
        this.categoryId = entity.getId();
        this.branch = entity.getBranch();
        this.code = entity.getCode();
        this.name = entity.getName();
        this.level = entity.getLevel();
        if(entity.getParentCategory() == null) {
            this.parentCategoryName = "대분류";
        } else {
            this.parentCategoryName = entity.getParentCategory().getName();
        }
        this.children = entity.getSubCategory() == null ? null :
                entity.getSubCategory().stream().collect(Collectors.toMap(Category::getName, CategoryDTO::new));
    }

    public Category toEntity () {
        return Category.builder()
                .branch(branch)
                .code(code)
                .level(level)
                .name(name)
                .build();
    }
}
