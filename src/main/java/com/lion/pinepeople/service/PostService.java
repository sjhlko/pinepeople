package com.lion.pinepeople.service;


import com.lion.pinepeople.domain.dto.post.*;
import com.lion.pinepeople.domain.entity.Post;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.PostRecommendRepository;
import com.lion.pinepeople.repository.PostRepository;
import com.lion.pinepeople.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRecommendRepository postRecommendRepository;

    private final PostRepository postRepository;
    private final UserRepository userRepository;



    public PostCreateResponse create(PostCreateRequest postCreateRequest, String userId) {

//        postRepository.save(findPost);

//        return PostUpdateResponse.of(findPost);

        Post savedPost = postRepository.save(postCreateRequest.of(validateUser(userId)));

//        log.info("postRepository.save(postCreateRequest.of(findUser)): {}", postRepository.save(postCreateRequest.of(findUser)));



        return PostCreateResponse.of(savedPost);

    }



    @Transactional
    public PostReadResponse getPost(Long postId, HttpServletRequest request, HttpServletResponse response) {

        Post post = validatePost(postId); // Post 객체 생성

        countHits(postId, request, response); // 조회수 쿠키 설정

//        post.updateHits(post.getHits()); // JPA내 변경 감지로 ++1된 조회수



      return PostReadResponse.of(postRepository.findById(postId));

    }


    public Page<PostReadResponse> getPostList(Pageable pageable, @RequestParam(required = false) String keyword) {

        if (keyword != null) {

            // 검색 키워드가 있을 경우
            searchByTitle(pageable, keyword);

        }

                 // 없을 경우 전체 목록 조회
           return PostReadResponse.of(postRepository.findAll(pageable));

    }


//    public Page<PostReadResponse> getMyPosts(Pageable pageable, String userId) {
//
//        return PostReadResponse.of(postRepository.findByUser(pageable, validateUser(userId)));
//
//    }


    public PostUpdateResponse update(Long postId, String userId, PostUpdateRequest postUpdateRequest) {

        validatePost(postId);
        validateUser(userId);
        verifyPostAuthor(userId, postId);

        Post updatedPost = validatePost(postId);

        updatedPost.updatePost(postUpdateRequest.getTitle(), postUpdateRequest.getBody());


//        postRepository.save(validatePost(postId));

        return PostUpdateResponse.of(validatePost(postId));

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
        }
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
            Post post = validatePost(postId);
            postRepository.save(post);
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
            Cookie newCookie = new Cookie("views", "[" + postId + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24);
            response.addCookie(newCookie);
        }

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


}
