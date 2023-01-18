package com.lion.pinepeople.controller;


import com.lion.pinepeople.domain.dto.PostCreateRequest;
import com.lion.pinepeople.domain.dto.PostCreateResponse;
import com.lion.pinepeople.domain.response.Response;
import com.lion.pinepeople.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RequestMapping("api/posts")
@RequiredArgsConstructor
@Api(tags = "Post API")
public class PostController {

    PostService postService;


    @Operation(summary = "게시물 등록")
    @PostMapping
    public Response<PostCreateResponse> create(@RequestBody PostCreateRequest postCreateRequest, @ApiIgnore Authentication authentication) {

        String userName = authentication.getName();

        return Response.success(new PostCreateResponse("게시물 등록 완료", postCreateResponse.getId()));
    }



//    @Operation(summary = "게시물 목록 조회")
//    @GetMapping("?param") // 이 부분?
//    public Response<PostReadResponse> read(@PathVariable Integer postId, @ApiIgnore Authentication authentication) {
//
//
//
//    }
//
//
//    @Operation(summary = "게시물 상세 조회")
//    @GetMapping("/{postID}")
//    public Response<PostReadResponse> detailed_read() {}
//
//
//
//    @Operation(summary = "게시물 수정")
//    @PatchMapping("/{postId}")
//    public Response<PostUpdateResponse> update() {}
//
//
//
//    @Operation(summary = "게시물 삭제")
//    @DeleteMapping("/{postId}")
//    public Response<PostDeleteResponse> delete() {}
//
//
//    }
//
//
//    @Operation(summary = "작성한 게시물 확인")
//    @GetMapping("/my")
//    public Response<Post> myFeed() {}

}

