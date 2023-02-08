package com.lion.pinepeople.controller;

import com.lion.pinepeople.domain.dto.postBookmark.PostBookmarkReadResponse;
import com.lion.pinepeople.domain.dto.postBookmark.PostBookmarkRequest;
import com.lion.pinepeople.domain.response.Response;
import com.lion.pinepeople.service.PostBookmarkService;
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
@RequestMapping("/pinepeople/posts/{postId}/bookmarks")
@RequiredArgsConstructor
@Api(tags = "게시물 북마크")
public class PostBookmarkController {


    private final PostBookmarkService postBookmarkService;

    @PostMapping
    @ApiOperation(value = "북마크 추가")
    public Response<PostBookmarkReadResponse> like(@PathVariable Long postId, @ApiIgnore Authentication authentication, PostBookmarkRequest postBookmarkRequest){

        return Response.success(postBookmarkService.bookmark(postId, authentication.getName(), postBookmarkRequest));
    }


    @GetMapping("/{postId}")
    @ApiOperation(value = "북마크 개수 조회")
    public Response<Long> BookmarkCount(@PathVariable Long postId) {

        return Response.success(postBookmarkService.countBookmark(postId));
    }


    @GetMapping("/my")
    @ApiOperation(value = "내가 북마크한 게시물 조회")
    public Response<Page<PostBookmarkReadResponse>> getMyBookmarks(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) @ApiIgnore Pageable pageable, @ApiIgnore Authentication authentication) {

        return Response.success(postBookmarkService.getMyBookmarks(pageable, authentication.getName()));

    }


}
