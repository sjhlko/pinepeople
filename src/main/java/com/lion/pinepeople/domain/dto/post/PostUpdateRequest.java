package com.lion.pinepeople.domain.dto.post;


import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdateRequest {


    private String title;

    private String body;


}
