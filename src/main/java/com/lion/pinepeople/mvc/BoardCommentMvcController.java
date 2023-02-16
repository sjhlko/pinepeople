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
import retrofit2.http.Path;

import javax.servlet.http.HttpServletRequest;
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

    @PostMapping("/register")
    public String createComment(Authentication authentication, CommentCreateRequest commentCreateRequest, @PathVariable Long postId) {
        log.info("authentication.getName() :{}", authentication.getName());
        log.info("postId: {}", postId);
        log.info("commentCreateRequest: {}", commentCreateRequest);
        commentService.createComment(authentication.getName(), postId, commentCreateRequest);
        return "redirect:/pinepeople/board/" + postId;
    }

    @PostMapping("/update/{commentId}")
    public String updatePost(CommentUpdateRequest commentUpdateRequest,
                             @PathVariable Long postId, @PathVariable Long commentId, Authentication authentication) {
        commentService.updateComment(authentication.getName(), commentId, postId, commentUpdateRequest.getComment());
        return "redirect:/pinepeople/board/" + postId;
    }

    @GetMapping("/delete/{commentId}")
    public String deleteComment(@PathVariable Long commentId, @PathVariable Long postId, Authentication authentication) {
        commentService.deleteComment(authentication.getName(), postId, commentId);
        return "redirect:/pinepeople/board/" + postId;
    }

    @GetMapping("/delete/{commentId}/{userId}")
    public String viewComment(Authentication authentication, @PathVariable Long postId, @PathVariable Long commentId, @PathVariable Long userId, Model model, HttpServletRequest request, HttpServletResponse response, Pageable pageable) throws Exception {
        validateUser(authentication, userId, response);
        commentService.deleteComment(authentication.getName(), postId, commentId);
        commentService.readCommentPage(pageable, postId);
        model.addAttribute("comment", commentService.deleteComment(authentication.getName(), postId, commentId));
        model.addAttribute(new Comment());
        model.addAttribute("post", postService.getPost(postId, request, response));
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
