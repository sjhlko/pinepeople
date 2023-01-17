package com.lion.pinepeople.controller;

import com.lion.pinepeople.domain.dto.UserJoinRequest;
import com.lion.pinepeople.domain.dto.UserJoinResponse;
import com.lion.pinepeople.domain.response.Response;
import com.lion.pinepeople.service.UserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Api(tags = "User API")
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest userJoinRequest){
        return null;
    }
}
