package com.lion.pinepeople.domain.post;


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
