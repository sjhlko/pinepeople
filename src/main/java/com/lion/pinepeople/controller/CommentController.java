package com.lion.pinepeople.controller;

import com.lion.pinepeople.domain.dto.comment.*;
import com.lion.pinepeople.domain.response.Response;
import com.lion.pinepeople.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;


@RestController
@RequestMapping("/pinepeople/api/posts/{postId}/comments")
@RequiredArgsConstructor
@Api(tags = "Comment API")
public class CommentController {

    private final CommentService commentService;

    /**
     * 새 댓글 작성
     *
     * @param authentication       로그인한 회원 id
     * @param postId               게시물 id
     * @param commentCreateRequest 입력할 댓글 내용
     * @return 생성된 댓글의 정보 리턴
     */
    @PostMapping
    @ApiOperation(value = "댓글 작성")
    public Response<CommentCreateResponse> create(@ApiIgnore Authentication authentication, @PathVariable Long postId, @RequestBody CommentCreateRequest commentCreateRequest) {
        return Response.success(commentService.createComment(authentication.getName(), postId, commentCreateRequest));
    }

    /**
     * 댓글 페이징 조회
     *
     * @param pageable 페이징
     * @param postId   해당 게시물 id
     * @return 해당 게시물에 페이징 처리된 댓글 리턴
     */
    @GetMapping
    @ApiOperation(value = "댓글 목록 조회")
    public Response<Page<CommentReadResponse>> read(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) @ApiIgnore Pageable pageable, @PathVariable Long postId) {
        return Response.success(commentService.readCommentPage(pageable, postId));
    }

//    @GetMapping("/my")
//    @ApiOperation(value = "내가 작성한 댓글 목록 조회")
//    public Response<Page<CommentReadResponse>> read(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) @ApiIgnore Pageable pageable, @PathVariable Long postId, Long userId) {
//        return Response.success(commentService.readCommentPage(pageable, postId));
//    }

    /**
     * 댓글 수정
     *
     * @param authentication 로그인한 회원 id
     * @param postId         해당 게시물 id
     * @param commentId      수정할 댓글 id
     * @param body           수정할 댓글 내용
     * @return 수정된 댓글의 정보 리턴
     */
    @ApiOperation(value = "댓글 수정")
    @PutMapping("/{commentId}")
    public Response<CommentUpdateResponse> update(@ApiIgnore Authentication authentication, @PathVariable Long postId, @PathVariable Long commentId, @RequestBody String body) {
        return Response.success(commentService.updateComment(authentication.getName(), postId, commentId, body));
    }

    /**
     * 댓글 삭제
     *
     * @param authentication 로그인한 회원 id
     * @param postId         해당 게시물 id
     * @param commentId      삭제할 댓글 id
     * @return 삭제된 댓글의 정보 리턴
     */
    @DeleteMapping("/{commentId}")
    @ApiOperation(value = "댓글 삭제")
    public Response<CommentDeleteResponse> delete(@ApiIgnore Authentication authentication, @PathVariable Long postId, @PathVariable Long commentId) {
        return Response.success(commentService.deleteComment(authentication.getName(), postId, commentId));
    }

}
