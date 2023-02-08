package com.lion.pinepeople.config.security.userdetail;

import com.lion.pinepeople.domain.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.security.Principal;

@Getter
@NoArgsConstructor
public class UserPrinclial implements Principal {

    private String id;
    private String nickName;
    private String role;

    public UserPrinclial(User user){
        this.id = String.valueOf(user.getId());
        this.nickName = user.getName();
        this.role = user.getRole().toString();
    }
    @Override
    public String getName() {
        return this.id;
    }
}
