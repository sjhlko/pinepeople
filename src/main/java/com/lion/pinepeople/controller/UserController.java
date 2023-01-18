package com.lion.pinepeople.controller;

import com.lion.pinepeople.domain.dto.user.delete.UserDeleteResponse;
import com.lion.pinepeople.domain.dto.user.join.UserJoinRequest;
import com.lion.pinepeople.domain.dto.user.join.UserJoinResponse;
import com.lion.pinepeople.domain.dto.user.login.UserLoginRequest;
import com.lion.pinepeople.domain.dto.user.login.UserLoginResponse;
import com.lion.pinepeople.domain.dto.user.update.UserUpdateRequest;
import com.lion.pinepeople.domain.dto.user.update.UserUpdateResponse;
import com.lion.pinepeople.domain.dto.user.role.UserRoleResponse;
import com.lion.pinepeople.domain.response.Response;
import com.lion.pinepeople.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Api(tags = "User API")
public class UserController {

    private final UserService userService;

    /**
     * 회원가입 메서드
     *
     * @param userJoinRequest name, email, password, address, phone, birth(yyyy-MM-dd)
     * @return userId, message
     */
    @PostMapping("/join")
    @ApiOperation(value = "회원가입")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest userJoinRequest) {
        UserJoinResponse userJoinResponse = userService.join(userJoinRequest);
        return Response.success(userJoinResponse);
    }

    /**
     * 로그인 메서드
     *
     * @param userLoginRequest email, password
     * @return jwt
     */
    @PostMapping("/login")
    @ApiOperation(value = "로그인")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest userLoginRequest) {
        UserLoginResponse userLoginResponse = userService.login(userLoginRequest);
        return Response.success(userLoginResponse);
    }

    /**
     * 계정 등급 변경 메서드
     *
     * @param authentication userId
     * @param id             계정 등급을 변경할 userId
     * @return userName, message
     */
    @PostMapping("/{id}/change-role")
    @ApiOperation(value = "계정 등급 변경")
    public Response<UserRoleResponse> changeRole(Authentication authentication, @PathVariable Long id) {
        UserRoleResponse userRoleResponse = userService.changeRole(authentication.getName(), id);
        return Response.success(userRoleResponse);
    }

    /**
     * 유저 수정 메서드
     *
     * @param authentication    수정하는 유저 id
     * @param id                수정할 유저 id
     * @param userUpdateRequest name, address, phone, birth
     * @return UserUpdateResponse message userId
     */
    @PatchMapping("/{id}")
    @ApiOperation(value = "유저 수정")
    public Response<UserUpdateResponse> modify(Authentication authentication, @PathVariable Long id, @RequestBody UserUpdateRequest userUpdateRequest) {
        UserUpdateResponse userModifyResponse = userService.modify(authentication.getName(), id, userUpdateRequest);
        return Response.success(userModifyResponse);
    }

    /**
     * 유저 삭제 메서드
     *
     * @param authentication 삭제하는 유저 id
     * @param id             삭제할 유저 id
     * @return UserDeleteResponse message userId
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "유저 삭제")
    public Response<UserDeleteResponse> delete(Authentication authentication, @PathVariable Long id) {
        UserDeleteResponse userDeleteResponse = userService.delete(authentication.getName(), id);
        return Response.success(userDeleteResponse);
    }
}
