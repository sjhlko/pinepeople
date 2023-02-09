package com.lion.pinepeople.domain.dto.user.myInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lion.pinepeople.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class MyInfoResponse {
    private Long userId;
    private String userName;
    private String email;
    private String phone;
    private String address;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;
    private Integer point;
    private Double brixFiguer;
    private String brixName;
    private String profileImg;

    public static MyInfoResponse of(User user) {
        return MyInfoResponse.builder()
                .userId(user.getId())
                .userName(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .birth(user.getBirth().toLocalDate())
                .point(user.getPoint())
                .brixFiguer(user.getBrix().getBrixFigure())
                .brixName(user.getBrix().getBrixName())
                .profileImg(user.getProfileImg())
                .build();
    }
}
