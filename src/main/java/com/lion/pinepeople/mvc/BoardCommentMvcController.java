package com.lion.pinepeople.mvc;

import com.lion.pinepeople.domain.dto.comment.*;
import com.lion.pinepeople.domain.dto.post.PostReadResponse;
import com.lion.pinepeople.domain.entity.Comment;
import com.lion.pinepeople.service.CommentService;
import com.lion.pinepeople.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("pinepeople/board/{postId}")
public class BoardCommentMvcController {

    private final CommentService commentService;
    private final PostService postService;


    // 댓글 작성 페이지
    @GetMapping("/register")
    public String viewComment(Model model, @PathVariable Long postId, Pageable pageable) {

        PostReadResponse postReadResponse = postService.getPost(postId);
        Page<CommentReadResponse> comments = commentService.readCommentPage(pageable, postId);

        model.addAttribute("post", postService.getPost(postId));
        model.addAttribute("comment", commentService.readCommentPage(pageable, postId));
        model.addAttribute("commentCreateRequest", new CommentCreateRequest());

        return "board/post";

    }

    // 댓글 작성한 페이지
    @PostMapping("/register")
    public String createComment(Authentication authentication, Model model, CommentCreateRequest commentCreateRequest, @PathVariable Long postId) { // <- 파라미터에 postId를 넣는 게 맞나?

        log.info("authentication.getName() :{}", authentication.getName());
        log.info("postId: {}", postId);
        log.info("commentCreateRequest: {}", commentCreateRequest);

        CommentCreateResponse commentCreateResponse = commentService.createComment(authentication.getName(), postId, commentCreateRequest);

        return "redirect:/pinepeople/board/" + commentCreateResponse.getId();
    }


    @GetMapping("/update/{commentId}")
    public String updatePost(Model model, @PathVariable Long commentId, Authentication authentication) {

        model.addAttribute("commentUpdateRequest", new CommentUpdateRequest());

        return "board/update";
    }


    @PostMapping("/update/{commentId}")
    public String updatePost(@Validated @ModelAttribute Comment comment,
                             @PathVariable Long postId, @PathVariable Long commentId, Authentication authentication) {


        CommentUpdateResponse commentUpdateResponse = commentService.updateComment(authentication.getName(), commentId, postId, comment.getBody());

        return "redirect:/pinepeople/board/" + commentUpdateResponse.getId();
    }





    @GetMapping("/delete/{commentId}/{userId}")
    public String viewComment(Authentication authentication, @PathVariable Long postId, @PathVariable Long commentId, @PathVariable Long userId, Model model, HttpServletResponse response, Pageable pageable) throws Exception {

        validateUser(authentication, userId, response);

        commentService.deleteComment(authentication.getName(), postId, commentId);
        commentService.readCommentPage(pageable, postId);

        model.addAttribute("comment", commentService.deleteComment(authentication.getName(), postId, commentId));
        model.addAttribute(new Comment());
        model.addAttribute("post", postService.getPost(postId));

        return "redirect:/pinepeople/post/post/" + postId;

    }


    private void validateUser(Authentication authentication, @PathVariable Long userId, HttpServletResponse response) throws IOException {
        if (Long.parseLong(authentication.getName()) != userId) {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('댓글 삭제 권한이 없습니다.'); history.go(-1);</script>");
            out.flush();
        }
    }


}
