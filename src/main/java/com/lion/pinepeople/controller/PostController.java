package com.lion.pinepeople.controller;

import com.lion.pinepeople.domain.dto.post.*;
import com.lion.pinepeople.domain.response.Response;
import com.lion.pinepeople.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;


@RestController
@RequestMapping("/pinepeople/posts")
@Api(tags = "게시물")
@Slf4j
@RequiredArgsConstructor

public class PostController {


    private final PostService postService;



    @ApiOperation(value = "게시물 등록")
    @PostMapping
    public Response<PostCreateResponse> create (@RequestBody PostCreateRequest postCreateRequest, @ApiIgnore Authentication authentication) {
        log.info("postRequest: {}", postCreateRequest);
        log.info("authentication: {}", authentication.getName());

        return Response.success(postService.create(postCreateRequest, authentication.getName()));
    }


    @ApiOperation(value = "게시물 상세 조회")
    @GetMapping("/{id}")
    public Response<PostReadResponse> getPost(@PathVariable Long id) {

        return Response.success(postService.getPost(id));

    }


    @ApiOperation(value = "게시물 목록 조회")
    @GetMapping
    public Response<Page<PostReadResponse>> getPostList (@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) @ApiIgnore Pageable pageable) {

        return Response.success(postService.getPostList(pageable));

    }


    @ApiOperation(value = "내가 작성한 게시물 조회")
    @GetMapping("/my")
    public Response<Page<PostReadResponse>> getMyPosts (@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) @ApiIgnore Pageable pageable, @ApiIgnore Authentication authentication) {
        return Response.success(postService.getMyPosts(pageable, authentication.getName())); // 인증(Authentication) : 보호된 리소스에 접근한 주체가 애플리케이션의 작업을 수행해도 되는 유저인지 확인한다.

    }


    @ApiOperation(value = "게시물 수정")
    @PutMapping("/{id}")
    public Response<PostUpdateResponse> update (@RequestBody PostUpdateRequest postUpdateRequest, @PathVariable Long id, @ApiIgnore Authentication authentication, MultipartFile file) {

        return Response.success(postService.update(id, authentication.getName(), postUpdateRequest));

    }



    @ApiOperation(value = "게시물 삭제")
    @DeleteMapping("{id}")
    public Response<PostDeleteResponse> delete (@PathVariable Long id, @ApiIgnore Authentication authentication) {

        return Response.success(postService.delete(id, authentication.getName()));

    }

    @ApiOperation(value = "게시물 키워드로 검색")
    @GetMapping("/keyword")
    public Response<Page<PostReadResponse>> searchByKeyword (@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) @ApiIgnore Pageable pageable, @RequestParam String keyword) {

        return Response.success(postService.searchByKeyword(pageable, keyword));

    }

    @ApiOperation(value = "게시물 제목으로 검색")
    @GetMapping("/title")
    public Response<Page<PostReadResponse>> searchByTitle (@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) @ApiIgnore Pageable pageable, @RequestParam String keyword) {

        return Response.success(postService.searchByTitle(pageable, keyword));

    }

}