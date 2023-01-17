package com.lion.pinepeople.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserJoinRequest {
    private String name;
    private String email;
    private String password;
    private String address;
    //나머지 입력 사항은 기본틀 구현 이후 작성
}
