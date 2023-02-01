package com.lion.pinepeople.controller;

import com.lion.pinepeople.domain.dto.post.PostReadResponse;
import com.lion.pinepeople.domain.dto.post.PostRequest;
import com.lion.pinepeople.domain.dto.post.PostResponse;
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
import springfox.documentation.annotations.ApiIgnore;


@RestController
@RequestMapping("/api/posts")
@Api(tags = "게시물")
@Slf4j
@RequiredArgsConstructor

public class PostController {

    private final PostService postService;


    @ApiOperation(value = "게시물 등록")
    @PostMapping
    public Response<PostResponse> create (@RequestBody PostRequest postRequest, @ApiIgnore Authentication authentication) {
        log.info("postRequest: {}", postRequest);
        log.info("authentication.getName(): {}", authentication.getName());
        String userId = authentication.getName(); // getName() String userId를 가져옴

        // DTO -> 서비스에서 처리 후 entity 변환 후 db 저장
        PostResponse postResponse = postService.create(userId, postRequest.getTitle(), postRequest.getBody());

        return Response.success(postResponse); // 객체

    }


    @ApiOperation(value = "게시물 상세 조회")
    @GetMapping("/{id}")
    public Response<PostReadResponse> getPost(@PathVariable String id) {

        PostReadResponse postReadResponse = postService.getPost(Long.parseLong(id));

        return Response.success(postReadResponse);

    }


    @ApiOperation(value = "게시물 목록 조회")
    @GetMapping
    public Response<Page<PostReadResponse>> getPostList(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) @ApiIgnore Pageable pageable) {

        Page<PostReadResponse> postGetResponsePage = postService.getPostList(pageable);

        return Response.success(postGetResponsePage);

    }


    @ApiOperation(value = "마이 피드") // 내가 작성한 게시물 조회
    @GetMapping("/my")
    public Response<Page<PostReadResponse>> getMyPosts(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) @ApiIgnore Pageable pageable, @ApiIgnore Authentication authentication) {
        String id = authentication.getName(); // Authentication(인증): 보호된 리소스에 접근한 대상이 작업 수행 대상 주체인지 확인
        Page<PostReadResponse> postGetResponsePage = postService.getMyPosts(pageable, Long.parseLong(id));
        return Response.success(postGetResponsePage);

    }


    @ApiOperation(value = "게시물 수정")
    @PutMapping("/{id}")
    public Response<PostResponse> update(@RequestBody PostRequest postRequest, @PathVariable Long id, @ApiIgnore Authentication authentication) {
        log.info("수정 컨트롤러 postRequest: {}", postRequest);

        // 회원 이름 가져오기
        log.info("authentication.getName(): {}", authentication.getName());
        String userId = authentication.getName(); // <- 원래 String userName이었음

        PostResponse postResponse = postService.update(id, userId, postRequest.getTitle(), postRequest.getBody());

        return Response.success(PostResponse.convertToDto(postResponse.getMessage(), postResponse.getId()));
    }



    @ApiOperation(value = "게시물 삭제")
    @DeleteMapping("/{id}")
    public Response<PostResponse> delete(@PathVariable String id, @ApiIgnore Authentication authentication) {

        String userId = authentication.getName();
        PostResponse postResponse = postService.delete(Long.parseLong(id), userId);

        return Response.success(PostResponse.convertToDto(postResponse.getMessage(), postResponse.getId()));

    }



}