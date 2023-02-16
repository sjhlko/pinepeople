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
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/pinepeople/api/posts")
@Api(tags = "Post API")
@Slf4j
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * 새 게시물 생성
     * 파티 생성시 category 와 participant 에도 자동적으로 해당하는 내용이 생성된다.
     *
     * @param postCreateRequest 등록할 게시물 내용
     * @param authentication    로그인한 회원 id
     * @return 생성된 게시물 및 회원 정보 리턴
     **/
    @ApiOperation(value = "게시물 등록")
    @PostMapping
    public Response<PostCreateResponse> create(@RequestBody PostCreateRequest postCreateRequest, @ApiIgnore Authentication authentication) {
        return Response.success(postService.create(postCreateRequest, authentication.getName()));
    }

    /**
     * 게시물 상세 조회
     *
     * @param postId   조회(검색)할 게시물 id
     * @param request, HTTP 헤더 및 데이터
     * @param response HTTP 응답 코드를 지정해주고 헤더와 바디를 생성
     * @return 해당 게시물 상세 정보 리턴
     */
    @ApiOperation(value = "게시물 상세 조회")
    @GetMapping("/{postId}")
    public Response<PostReadResponse> getPost(@PathVariable Long postId, HttpServletRequest request, HttpServletResponse response) {
        return Response.success(postService.getPost(postId, request, response));
    }

    /**
     * 모든 게시물 목록 조회
     *
     * @param pageable 페이징
     * @param keyword  게시물 내용 키워드
     * @return 키워드가 없을 경우, 존재하는 모든 게시물을 페이징해서 리턴. 키워드가 있을 경우, 키워드가 포함된 모든 게시물들을 페이징 해서 리턴.
     */
    @ApiOperation(value = "게시물 목록 조회")
    @GetMapping
    public Response<Page<PostReadResponse>> getPostList(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) @ApiIgnore Pageable pageable, @RequestParam(required = false) String keyword) {
        return Response.success(postService.getPostList(pageable, keyword));
    }

    /**
     * 게시물 수정
     *
     * @param postUpdateRequest 수정할 게시물 내용
     * @param authentication    로그인한 회원 id
     * @return 수정한 게시물 상세정보 리턴
     */
    @ApiOperation(value = "게시물 수정")
    @PutMapping("/{postId}")
    public Response<PostUpdateResponse> update(@RequestBody PostUpdateRequest postUpdateRequest, @PathVariable Long postId, @ApiIgnore Authentication authentication) {
        return Response.success(postService.update(postId, authentication.getName(), postUpdateRequest));
    }

    /**
     * 게시물 삭제
     *
     * @param postId         삭제할 게시물 id
     * @param authentication 로그인한 회원 id
     * @return 삭제한 게시물 정보 리턴
     */
    @ApiOperation(value = "게시물 삭제")
    @DeleteMapping("{postId}")
    public Response<PostDeleteResponse> delete(@PathVariable Long postId, @ApiIgnore Authentication authentication) {
        return Response.success(postService.delete(postId, authentication.getName()));
    }

    /**
     * 특정 내용이 포함된 게시물 검색
     * @param pageable 페이징
     * @param keyword 게시물 내용 키워드
     *
     * @return 검색조건에 부합하는 게시물의 상세정보를 페이징하여 리턴
     */
//    @ApiOperation(value = "게시물 내용으로 검색")
//    @GetMapping("/search")
//    public Response<Page<PostReadResponse>> searchByTitle (@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) @ApiIgnore Pageable pageable, @RequestParam String keyword) {
//        return Response.success(postService.searchByTitle(pageable, keyword));
//    }

}