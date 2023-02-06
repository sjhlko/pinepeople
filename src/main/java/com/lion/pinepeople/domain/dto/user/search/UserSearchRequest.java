package com.lion.pinepeople.domain.dto.user.search;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserSearchRequest {
    private String keyword;
}
