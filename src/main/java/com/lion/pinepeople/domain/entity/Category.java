package com.lion.pinepeople.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String branch;

    private String code;

    private String name;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name ="parent_cagegory_id")
    private Category parentCategory;

    @OneToMany (mappedBy = "parentCategory", cascade = CascadeType.ALL)
    private List<Category> subCategory = new ArrayList<>();

    private Integer level;



    @Builder
    public Category(String branch, String code, String name, Integer level,Category parentCategory) {
        this.branch = branch;
        this.code = code;
        this.name = name;
        this.level = level;
        this.parentCategory = parentCategory;
    }
}
