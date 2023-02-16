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

    /**
     * 게시물에 추천 추가
     *
     * @param postId               추천을 추가할 게시물 id
     * @param authentication       로그인한 회원 id
     * @param postRecommendRequest 추천 정보
     * @return 추가된 게시물 추천 정보 리턴
     */
    @PostMapping("/{postId}/recommend")
    @ApiOperation(value = "게시물 추천 추가")
    public Response<PostRecommendResponse> recommend(@PathVariable Long postId, @ApiIgnore Authentication authentication, PostRecommendRequest postRecommendRequest) {
        return Response.success(postRecommendService.addRecommend(postId, authentication.getName(), postRecommendRequest));
    }

    /**
     * 해당 게시물에 존재하는 모든 추천수 조회
     *
     * @param postId 추천개수를 조회할 게시물 id
     * @return 해당 게시물의 추천수를 카운트하여 리턴
     */
    @GetMapping("/{postId}/recommends")
    @ApiOperation(value = "해당 게시물의 추천수 조회")
    public Response<Integer> getRecommendsCount(@PathVariable Long postId) {
        return Response.success(postRecommendService.getRecommendsCount(postId));
    }

    /**
     * 게시물에 추천 삭제
     *
     * @param postId         추천을 삭제하고자 하는 게시물 id
     * @param authentication 로그인한 회원 id
     * @return 삭제한 게시물 추천 정보 리턴
     */
    @DeleteMapping("/{postId}/recommend")
    @ApiOperation(value = "게시물 추천 삭제")
    public Response<PostRecommendResponse> deleteRecommend(@PathVariable Long postId, @ApiIgnore Authentication authentication, PostRecommendRequest postRecommendRequest) {
        return Response.success(postRecommendService.deleteRecommend(postId, authentication.getName(), postRecommendRequest));
    }

}
