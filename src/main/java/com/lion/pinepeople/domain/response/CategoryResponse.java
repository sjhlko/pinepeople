package com.lion.pinepeople.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {

    private Long categoryId;
    private String branch;
    private String code;
    private String name;
    private String parentCategoryName;
    private Integer level;
}
