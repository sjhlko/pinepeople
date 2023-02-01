package com.lion.pinepeople.domain.dto.post;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class PostResponse {

    private String message;
    private Long id;
    private String userName;


    /***
     * convertToDto
     * Entity를 Dto로 변환
     * @param message
     * @param id
     * @return
     */
    public static PostResponse convertToDto(String message, Long id) {
        return PostResponse.builder()
                .message(message)
                .id(id)
                .build();
    }
}