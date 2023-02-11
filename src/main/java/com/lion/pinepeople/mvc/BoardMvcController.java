package com.lion.pinepeople.mvc;


import com.lion.pinepeople.domain.dto.post.*;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/pinepeople/board")
public class BoardMvcController {


    private final PostService postService;
    private final PostRecommendService postRecommendService;



    @GetMapping("/register")
    public String registerPost(Model model) {

        model.addAttribute("postCreateRequest", new PostCreateRequest());

        return "board/register";

    }


    @PostMapping("/register")
    public String doRegisterPost(PostCreateRequest postCreateRequest, Authentication authentication) {
        PostCreateResponse postCreateResponse = postService.create(postCreateRequest, authentication.getName());
        return "redirect:/pinepeople/board/" + postCreateResponse.getId();
    }


    @GetMapping("/{postId}")
    public String getPostDetail(@PathVariable Long postId, Model model) {

        PostReadResponse postReadResponse = postService.getPost(postId);
        model.addAttribute("postReadResponse", postReadResponse);
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
    public String updatePost(Model model, @PathVariable Long postId, Authentication authentication) {

        model.addAttribute("postUpdateRequest", new PostUpdateRequest());

        return "board/update";
    }


    @PostMapping("/update/{postId}")
    public String updatePost(@Validated @ModelAttribute PostUpdateRequest postUpdateRequest,
                             @PathVariable Long postId, Authentication authentication) {
        log.info("authentication : {}",authentication.getName());

        PostUpdateResponse postUpdateResponse = postService.update(postId, authentication.getName(), postUpdateRequest);

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