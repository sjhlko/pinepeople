package com.lion.pinepeople.service;


import com.lion.pinepeople.domain.dto.post.*;
import com.lion.pinepeople.domain.entity.Post;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.PostRepository;
import com.lion.pinepeople.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    //private final static String VIEWCOOKIENAME = "alreadyViewCookie";

    @Transactional
    public PostCreateResponse create(PostCreateRequest postCreateRequest, String userId) {

        User findUser = validateUser(userId);

        Post savedPost = postRepository.save(postCreateRequest.of(findUser));

        return PostCreateResponse.of(savedPost);

    }


    public PostReadResponse getPost(Long postId, HttpServletRequest request, HttpServletResponse response) {

        validatePost(postId);

        postRepository.findById(postId);
        Post post = validatePost(postId);

        countHits(postId, request, response);
        post.updateHits(post.getHits()); // 변경 방지 ++1


        return PostReadResponse.of(postRepository.findById(postId));

    }





    public Page<PostReadResponse> getPostList(Pageable pageable) {

        return PostReadResponse.of(postRepository.findAll(pageable));

    }


    public Page<PostReadResponse> getMyPosts(Pageable pageable, String userId) {

        return PostReadResponse.of(postRepository.findByUser(pageable, validateUser(userId)));

    }



    public PostUpdateResponse update(Long postId, String userId, PostUpdateRequest postUpdateRequest) {

        validatePost(postId);
        validateUser(userId);
        verifyPostAuthor(userId, postId);

        Post updatedPost = validatePost(postId);

        updatedPost.updatePost(postUpdateRequest.getTitle(), postUpdateRequest.getBody());
        postRepository.saveAndFlush(updatedPost);


        return PostUpdateResponse.of(updatedPost);

    }


    public PostDeleteResponse delete(Long postId, String userId) {


        validatePost(postId);
        log.info("postId: {}", postId);
        validateUser(userId);
        log.info("userId: {}", userId);
        verifyPostAuthor(userId, postId);

        postRepository.deleteById(postId);

        return PostDeleteResponse.of(postId);

    }


    // 제목으로 게시물 검색
    public Page<PostReadResponse> searchByTitle(Pageable pageable, String keyword) {

        if (keyword == null) {
            searchByTitle(pageable, null);
        } else {
            searchByTitle(pageable, keyword);
        }


        return PostReadResponse.of(postRepository.findByTitleContaining(keyword, pageable));

    }


    public User validateUser(String userId) {
        return userRepository.findById(Long.parseLong(userId)).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });
    }

    public Post validatePost(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> {
            throw new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage());
        });
    }


    public void verifyPostAuthor(String userId, Long postId) {
        if (userRepository.findById(Long.parseLong(userId)).get().getId() != postRepository.findById(postId).get().getUser().getId()) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        };
    }


    private void countHits(Long postId, HttpServletRequest request, HttpServletResponse response) {

        Cookie oldCookie = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("views")) {
                    oldCookie = cookie;
                }
            }
        }

        if (oldCookie != null) {
            if (!oldCookie.getValue().contains("[" + postId.toString() + "]")) {
                Post post = validatePost(postId);
                post.updateHits(post.getHits());
                oldCookie.setValue(oldCookie.getValue() + "_[" + postId + "]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 60 * 24);
                response.addCookie(oldCookie);
            }
        } else {
            Post post = validatePost(postId);
            post.updateHits(post.getHits());
            Cookie newCookie = new Cookie("views","[" + postId + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24);
            response.addCookie(newCookie);
        }
    }

}
