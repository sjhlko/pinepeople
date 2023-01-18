package com.lion.pinepeople.controller;

import com.lion.pinepeople.domain.dto.user.join.UserJoinRequest;
import com.lion.pinepeople.domain.dto.user.join.UserJoinResponse;
import com.lion.pinepeople.domain.dto.user.login.UserLoginRequest;
import com.lion.pinepeople.domain.dto.user.login.UserLoginResponse;
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
     * @param userJoinRequest name, email, password, address
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
     * 계정 등급 변경
     *
     * @param authentication userId
     * @param id             계정 등급을 변경할 userId
     * @return userName, message
     */
    @PostMapping("/{id}/change-role")
    @ApiOperation(value = "계정 등급 변경")
    public Response<UserRoleResponse> changeRole(Authentication authentication, @PathVariable Long id) {
        UserRoleResponse userRoleResponse = userService.changeRole(id, authentication.getName());
        return Response.success(userRoleResponse);
    }
}
