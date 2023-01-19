package com.lion.pinepeople.domain.dto.category;

import com.lion.pinepeople.domain.dto.partyComment.PartyCommentResponse;
import com.lion.pinepeople.domain.entity.Category;
import com.lion.pinepeople.domain.entity.PartyComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SmallCategoryResponse {

    private Long categoryId;
    private String branch;
    private String code;
    private String name;


    public static SmallCategoryResponse of(Category category) {
        return SmallCategoryResponse.builder()
                .categoryId(category.getId())
                .branch(category.getBranch())
                .code(category.getCode())
                .name(category.getName())
                .build();
    }

    public Category toEntity () {
        return Category.builder()
                .id(categoryId)
                .branch(branch)
                .code(code)
                .name(name)
                .build();
    }
}
