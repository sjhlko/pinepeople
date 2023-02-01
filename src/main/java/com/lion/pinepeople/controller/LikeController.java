package com.lion.pinepeople.controller;

import com.lion.pinepeople.domain.dto.like.LikeResponse;
import com.lion.pinepeople.domain.response.Response;
import com.lion.pinepeople.service.LikeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;


@RestController
@RequestMapping("/api/posts/{postId}/likes")
@RequiredArgsConstructor
@Api(tags = "좋아요")
public class LikeController {

    private final LikeService likeService;


    @PostMapping
    @ApiOperation(value = "좋아요 추가")                                           // 경로 변수
    public Response<LikeResponse> like(@ApiIgnore Authentication authentication, @PathVariable Long postId) {

        String userId = authentication.getName(); // userId
        LikeResponse likeResponse = likeService.like(userId, postId);

        return Response.success(likeResponse); // 서비스에서 반환 받은 객체를 넣는다.
    }


    @GetMapping
    @ApiOperation(value = "좋아요 개수 조회")
    public Response<Long> likeCount(@PathVariable Long postId) {
        Long likeCount = likeService.countLike(postId);

        return Response.success(likeCount);
    }

}