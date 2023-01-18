package com.lion.pinepeople.domain.response;

import com.lion.pinepeople.domain.entity.PartyComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PartyCommentListResponse {

    private String userName;
    private String body;
    private String createdAt;


    /**map() 을 사용하여 알람엔티티의 페이징응답을 알람DTO페이징으로 변환**/
    public static Page<PartyCommentListResponse> toResponse(Page<PartyComment> comments){
        Page<PartyCommentListResponse> responses =
                comments.map(a -> PartyCommentListResponse.builder()
                        .userName(a.getUser().getName())
                        .body(a.getBody())
                        .createdAt(String.valueOf(a.getCreatedAt()))
                        .build());
        return responses;
    }
}
