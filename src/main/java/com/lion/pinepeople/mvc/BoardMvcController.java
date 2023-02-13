package com.lion.pinepeople.mvc;


import com.lion.pinepeople.domain.dto.comment.CommentCreateRequest;
import com.lion.pinepeople.domain.dto.comment.CommentReadResponse;
import com.lion.pinepeople.domain.dto.comment.CommentUpdateRequest;
import com.lion.pinepeople.domain.dto.post.*;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.service.CommentService;
import com.lion.pinepeople.service.PostRecommendService;
import com.lion.pinepeople.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/pinepeople/board")
public class BoardMvcController {


    private final PostService postService;
    private final CommentService commentService;
    private final PostRecommendService postRecommendService;


    @GetMapping("/register")
    public String registerPost(Model model) {


        model.addAttribute("postCreateRequest", new PostCreateRequest());

        return "board/register";

    }


    @PostMapping("/register")
    public String doRegisterPost(@Validated @ModelAttribute PostCreateRequest postCreateRequest, BindingResult bindingResult, Authentication authentication) {

        if (bindingResult.hasErrors()) {
            return "board/register";
        }
        PostCreateResponse postCreateResponse = null;
        try {
            postCreateResponse = postService.create(postCreateRequest, authentication.getName());
        } catch (AppException e) {
            bindingResult.reject("postRegisterFail", e.getMessage());
        }

        if (bindingResult.hasErrors()) {
            return "board/register";
        }

        return "redirect:/pinepeople/board/" + postCreateResponse.getId();
    }


    @GetMapping("/{postId}")
    public String getPostDetail(@PageableDefault(sort = "createdAt", direction = Sort.Direction.ASC) @ApiIgnore Pageable pageable, @PathVariable Long postId, Model model, HttpServletRequest request, HttpServletResponse response) {

        PostReadResponse postReadResponse = postService.getPost(postId, request, response);
        Page<CommentReadResponse> commentReadResponses = commentService.readCommentPage(pageable, postId);
        model.addAttribute("postReadResponse", postReadResponse);
        model.addAttribute("commentCreateRequest", new CommentCreateRequest());
        model.addAttribute("comments", commentReadResponses);
        model.addAttribute("commentUpdateRequest", new CommentUpdateRequest());
        return "board/post";

    }


    @GetMapping("/list")
    public String getPostList(@PageableDefault(page = 0, size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        Page<PostReadResponse> posts = null;
        posts = postService.getPostList(pageable);
        model.addAttribute("posts", posts);
        doPage(model, posts);
        return "board/list";
    }


    private void doPage(Model model, Page<PostReadResponse> posts) {
        /**페이징 처리**/
        int nowPage = posts.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 9, posts.getTotalPages());
        /**페이지 **/
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
    }


    @GetMapping("/update/{postId}")
    public String updatePost(Model model, @PathVariable Long postId, Authentication authenticationl, HttpServletRequest request, HttpServletResponse response) {

        PostReadResponse postReadResponse = postService.getPost(postId, request, response);
        model.addAttribute("postUpdateRequest", new PostUpdateRequest());
        model.addAttribute("postReadResponse", postReadResponse);

        return "board/update";
    }


    @PostMapping("/update/{postId}")
    public String updatePost(@Validated @ModelAttribute PostUpdateRequest postUpdateRequest, BindingResult bindingResult,
                             @PathVariable Long postId, Model model, Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        log.info("authentication : {}", authentication.getName());
        PostReadResponse postReadResponse = postService.getPost(postId, request, response);
        if (bindingResult.hasErrors()) {
            model.addAttribute("postReadResponse", postReadResponse);
            return "board/update";
        }

        PostUpdateResponse postUpdateResponse = null;
        try {
            postUpdateResponse = postService.update(postId, authentication.getName(), postUpdateRequest);
        } catch (AppException e) {
            bindingResult.reject("postUpdateFail", e.getMessage());
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("postReadResponse", postReadResponse);
            return "board/update";
        }

        return "redirect:/pinepeople/board/" + postUpdateResponse.getId();
    }


    @GetMapping("/delete/{postId}")
    public String boardDelete(@PathVariable Long postId, Authentication authentication) {

        log.info("postId: {}", postId);
        log.info("authentication.getName(): {}", authentication.getName());
        postService.delete(postId, authentication.getName());

        return "redirect:/pinepeople/board/list";
    }

//
//    @ApiOperation(value = "게시물 추천")
//    @ResponseBody
//    @PostMapping("/recommend")
//    public Response<PostRecommendResponse> recommend(@RequestBody PostRecommendRequest postRecommendRequest) {
//
//        PostRecommendResponse postRecommendResponse = postRecommendService.recommend(postRecommendRequest.getId());
//
//        return Response.success(postRecommendResponse);
//
//    }


}