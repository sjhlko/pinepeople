package com.lion.pinepeople.domain.dto.user.userInfo;

import com.lion.pinepeople.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UserInfoResponse {
    private Long userId;
    private String userName;
    private String email;
    private Double brixFiguer;
    private String brixName;

    private String profileImg;

    public static UserInfoResponse of(User user) {
        return UserInfoResponse.builder()
                .userId(user.getId())
                .userName(user.getName())
                .email(user.getEmail())
                .brixFiguer(user.getBrix().getBrixFigure())
                .brixName(user.getBrix().getBrixName())
                .profileImg(user.getProfileImg())
                .build();
    }
}
