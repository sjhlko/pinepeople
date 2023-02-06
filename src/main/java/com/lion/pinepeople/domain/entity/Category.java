package com.lion.pinepeople.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String branch; // 카테고리 종류

    private String code; // 카테고리 code

    private String name; // 카테고리 이름

    @ManyToOne
    @JoinColumn (name ="parent_category_id")
    private Category parentCategory;

    @OneToMany (mappedBy = "parentCategory", cascade = CascadeType.ALL)
    private List<Category> subCategory = new ArrayList<>();

    private Integer level; // 카테고리 계층 구조


    @Builder
    public Category(Long id,String branch, String code, String name, Integer level,Category parentCategory) {
        this.id = id;
        this.branch = branch;
        this.code = code;
        this.name = name;
        this.level = level;
        this.parentCategory = parentCategory;
    }

    /**자식 카테고리에 parentCategory값 넣기**/
    public void addParentCategory(Category rootCategory) {
        this.parentCategory = rootCategory;
    }

    /**자식카테고리의 level 넣기**/
    public void addLevel(Integer level) {
        this.level = level;
    }

    /**카테고리 이름 수정 변경감지 메서드**/
    public void changeCategoryName(String categoryName) {
        this.name = categoryName;
    }
}
