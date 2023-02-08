package com.lion.pinepeople.controller;


import com.lion.pinepeople.domain.dto.commentLike.CommentLikeReadResponse;
import com.lion.pinepeople.domain.dto.commentLike.CommentLikeRequest;
import com.lion.pinepeople.domain.dto.commentLike.CommentLikeResponse;
import com.lion.pinepeople.domain.response.Response;
import com.lion.pinepeople.service.CommentLikeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/pinepeople/comments/{commentId}/likes")
@RequiredArgsConstructor
@Api(tags = "댓글 좋아요")
public class CommentLikeController {


    private final CommentLikeService commentLikeService;

    @PostMapping
    @ApiOperation(value = "좋아요 추가")
    public Response<CommentLikeResponse> like(String userId, CommentLikeRequest commentLikeRequest, @PathVariable Long commentId){

        return Response.success(commentLikeService.like(commentId, commentId, userId, commentLikeRequest));
    }


    @GetMapping("/{commentId}")
    @ApiOperation(value = "좋아요 개수 조회")
    public Response<Long> likeCount(@PathVariable Long postId, @PathVariable Long commentId) {

        return Response.success(commentLikeService.countLikedComment(postId, commentId));
    }

    @GetMapping("/my")
    @ApiOperation(value = "내가 좋아요 누른 댓글 조회")
    public Response<Page<CommentLikeReadResponse>> getMyLikes(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) @ApiIgnore Pageable pageable, String userId) {

        return Response.success(commentLikeService.getMylikes(pageable, userId));
    }

}