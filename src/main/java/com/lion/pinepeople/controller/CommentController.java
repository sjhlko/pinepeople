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


    @PostMapping
    @ApiOperation(value = "댓글 작성")
    public Response<CommentCreateResponse> create(@ApiIgnore Authentication authentication, @PathVariable Long postId, @RequestBody CommentCreateRequest commentCreateRequest) {

        return Response.success(commentService.createComment(authentication.getName(), postId, commentCreateRequest));

    }


    @GetMapping
    @ApiOperation(value = "댓글 목록 조회")
    public Response<Page<CommentReadResponse>> read(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) @ApiIgnore Pageable pageable, @PathVariable Long postId) {

        return Response.success(commentService.readCommentPage(pageable, postId));

    }


    @GetMapping("/my")
    @ApiOperation(value = "내가 작성한 댓글 목록 조회")
    public Response<Page<CommentReadResponse>> read(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) @ApiIgnore Pageable pageable, @PathVariable Long postId, Long userId) {

        return Response.success(commentService.readCommentPage(pageable, postId));

    }


    @ApiOperation(value = "댓글 수정")
    @PutMapping("/{commentId}")
    public Response<CommentUpdateResponse> update(@ApiIgnore Authentication authentication, @PathVariable Long postId, @PathVariable Long commentId, @RequestBody String comment) {

        return Response.success(commentService.updateComment(authentication.getName(), postId, commentId, comment));
    }


    @DeleteMapping("/{commentId}")
    @ApiOperation(value = "댓글 삭제")
    public Response<CommentDeleteResponse> delete(@ApiIgnore Authentication authentication, @PathVariable Long postId, @PathVariable Long commentId) {

        return Response.success(commentService.deleteComment(authentication.getName(), postId, commentId));
    }



}
