package com.lion.pinepeople.domain.entity;

import com.lion.pinepeople.domain.dto.UserJoinRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Getter
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String name;
    private String email;
    private String password;
    private String address;
    private String phone;
    private LocalDateTime birth;
    private Integer warningCnt;
    private Integer point;

    public static User of(UserJoinRequest userJoinRequest){
        return User.builder()
                .name(userJoinRequest.getName())
                .email(userJoinRequest.getEmail())
                .build();
    }
}
