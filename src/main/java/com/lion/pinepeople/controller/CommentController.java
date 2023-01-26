package com.lion.pinepeople.controller;

import com.lion.pinepeople.domain.dto.comment.CommentDeleteResponse;
import com.lion.pinepeople.domain.dto.comment.CommentRequest;
import com.lion.pinepeople.domain.dto.comment.CommentResponse;
import com.lion.pinepeople.domain.dto.comment.CommentUpdateResponse;
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
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
@Api(tags = "댓글")
public class CommentController {

    private final CommentService commentService;


    @PostMapping
    @ApiOperation(value = "댓글 작성")
    public Response<CommentResponse> createComment(@ApiIgnore Authentication authentication, @PathVariable Long postId, @RequestBody CommentRequest commentRequest) {

        String userId = authentication.getName(); // 리소스 접근 가능한 회원 불러오기?
        CommentResponse commentResponse = commentService.createComment(userId, postId, commentRequest.getBody());
        return Response.success(commentResponse);

    }


    @GetMapping
    @ApiOperation(value = "댓글 목록 조회")
    public Response<Page<CommentResponse>> readCommentPage(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) @ApiIgnore Pageable pageable, @PathVariable Long postId) {

        Page<CommentResponse> commentResponses = commentService.readCommentPage(pageable, postId);

        return Response.success(commentResponses);

    }

    /***
     * getMyComments 내가 작성한 댓글 조회
     * @param pageable
     * @param authentication
     * @return
     */
    @ApiOperation(value = "마이 피드")
    @GetMapping("/my")
    public Response<Page<CommentResponse>> getMyComments(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) @ApiIgnore Pageable pageable, @ApiIgnore Authentication authentication) {
        String userId = authentication.getName();
        Page<CommentResponse> getCommentPage = commentService.readMyComments(pageable, userId);
        return Response.success(getCommentPage);

    }


    @ApiOperation(value = "댓글 수정")
    @PutMapping("/{commentId}")
    public Response<CommentUpdateResponse> update(@ApiIgnore Authentication authentication, @PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentRequest commentRequest) {
        String userId = authentication.getName();
        CommentUpdateResponse commentUpdateResponse = commentService.update(userId, postId, commentId, commentRequest.getBody());

        return Response.success(commentUpdateResponse);
    }


    @DeleteMapping("/{commentId}")
    @ApiOperation(value = "댓글 삭제")
    public Response<CommentDeleteResponse> delete(@ApiIgnore Authentication authentication, @PathVariable Long postId, @PathVariable Long commentId) {
        String userId = authentication.getName();
        CommentDeleteResponse commentDeleteResponse = commentService.delete(userId, postId, commentId);
        return Response.success(commentDeleteResponse);
    }


}
