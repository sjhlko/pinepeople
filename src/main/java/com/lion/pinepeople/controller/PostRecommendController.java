package com.lion.pinepeople.controller;

import com.lion.pinepeople.domain.dto.postRecommend.PostRecommendRequest;
import com.lion.pinepeople.domain.dto.postRecommend.PostRecommendResponse;
import com.lion.pinepeople.domain.response.Response;
import com.lion.pinepeople.service.PostRecommendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Transactional
@RestController
@RequestMapping("/pinepeople/api/posts")
@RequiredArgsConstructor
@Api(tags = "Recommend API")
public class PostRecommendController {


    private final PostRecommendService postRecommendService;

    @PostMapping("/{postId}/recommend")
    @ApiOperation(value = "게시물 추천 추가")
    public Response<PostRecommendResponse> recommend(@PathVariable Long postId, @ApiIgnore Authentication authentication, PostRecommendRequest postRecommendRequest){

        return Response.success(postRecommendService.addRecommend(postId, authentication.getName(), postRecommendRequest));
    }




//    @PostMapping
//    @ApiOperation(value = "북마크 추가")
//    public Response<PostRecommendResponse> recommend(@PathVariable Long postId, @ApiIgnore Authentication authentication, PostRecommendRequest postRecommendRequest){
//
//        postRecommendService.recommend(postId, authentication.getName(), postRecommendRequest);
//
//        return Response.success(new PostRecommendResponse(postId));
//    }
//
//
    @GetMapping("/{postId}/recommends")
    @ApiOperation(value = "해당 게시물의 추천수 조회")
    public Response<Integer> getRecommendsCount(@PathVariable Long postId) {

        return Response.success(postRecommendService.getRecommendsCount(postId));
    }

//
//
//    @GetMapping("/my")
//    @ApiOperation(value = "내가 북마크한 게시물 조회")
//    public Response<Page<PostRecommendResponse>> getMyBookmarks(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) @ApiIgnore Pageable pageable, @ApiIgnore Authentication authentication) {
//
//        return Response.success(postRecommendService.getMyRecommends(pageable, authentication.getName()));
//
//    }


}
