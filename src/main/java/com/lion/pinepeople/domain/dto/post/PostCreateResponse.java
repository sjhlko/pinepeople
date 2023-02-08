package com.lion.pinepeople.domain.dto.post;

 import com.lion.pinepeople.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateResponse {


    private Long id;

    public static PostCreateResponse of (Post savedPost) {
        return PostCreateResponse.builder()
                .id(savedPost.getId())
                .build();
    }

}
