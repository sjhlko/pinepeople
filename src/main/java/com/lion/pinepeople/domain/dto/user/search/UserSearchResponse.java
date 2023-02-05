package com.lion.pinepeople.domain.dto.user.search;

import com.lion.pinepeople.domain.dto.user.userInfoList.UserInfoListResponse;
import com.lion.pinepeople.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UserSearchResponse {
    private Long userId;
    private String userName;
    private String email;
    private Double brixFiguer;
    private String brixName;

    public static UserSearchResponse of(User user) {
        return UserSearchResponse.builder()
                .userId(user.getId())
                .userName(user.getName())
                .email(user.getEmail())
                .brixFiguer(user.getBrix().getBrixFigure())
                .brixName(user.getBrix().getBrixName())
                .build();
    }

    public static Page<UserSearchResponse> ofList(Page<User> users) {
        return users.map(user -> UserSearchResponse.builder()
                .userId(user.getId())
                .userName(user.getName())
                .email(user.getEmail())
                .brixFiguer(user.getBrix().getBrixFigure())
                .brixName(user.getBrix().getBrixName())
                .build());
    }
}
