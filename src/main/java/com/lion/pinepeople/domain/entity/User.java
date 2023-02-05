package com.lion.pinepeople.domain.entity;

import com.lion.pinepeople.domain.dto.user.join.UserJoinRequest;
import com.lion.pinepeople.domain.dto.user.update.UserUpdateRequest;
import com.lion.pinepeople.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Getter
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE user SET deleted_at = CURRENT_TIMESTAMP where user_id = ?")
public class User extends BaseEntity {
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

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToOne(mappedBy = "user")
    private Brix brix;

    public static User of(UserJoinRequest userJoinRequest, String password) {
        return User.builder()
                .name(userJoinRequest.getName())
                .email(userJoinRequest.getEmail())
                .password(password)
                .address(userJoinRequest.getAddress())
                .phone(userJoinRequest.getPhone())
                .birth(userJoinRequest.getBirth().atStartOfDay())
                .warningCnt(0)
                .point(0)
                .role(UserRole.USER)
                .build();
    }

    public void updateRole(UserRole userRole) {
        this.role = userRole;
    }

    public void updateUser(UserUpdateRequest userUpdateRequest) {
        this.name = userUpdateRequest.getName();
        this.address = userUpdateRequest.getAddress();
        this.phone = userUpdateRequest.getPhone();
        this.birth = userUpdateRequest.getBirth().atStartOfDay();
    }

    public void updateWarningCnt() {
        this.warningCnt += 1;
    }

    public void updatePoint(int point) {
        this.point = point;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
