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
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * 새 게시물 등록
     *
     * @param postCreateRequest 등록할 게시물의 저장된 정보
     * @param userId            게시물을 등록할 회원의 userId
     * @return 생성된 게시물의 정보 리턴
     */
    @Transactional
    public PostCreateResponse create(PostCreateRequest postCreateRequest, String userId) {
        Post savedPost = postRepository.save(postCreateRequest.of(validateUser(userId)));
        return PostCreateResponse.of(savedPost);
    }

    /**
     * 게시물 상세 조회
     *
     * @param postId   조회(검색)할 게시물 id
     * @param request, HTTP 헤더 및 데이터
     * @param response HTTP 응답 코드를 지정해주고 헤더와 바디를 생성
     * @return 해당 게시물 상세 정보를 페이징해서 리턴
     */
    @Transactional
    public PostReadResponse getPost(Long postId, HttpServletRequest request, HttpServletResponse response) {
        validatePost(postId);
        countHits(postId, request, response);
        return PostReadResponse.of(postRepository.findById(postId));
    }

    /**
     * 존재하는 모든 게시물 정보 조회
     *
     * @return 모든 게시물의 상세 정보를 페이징하여 리턴
     */
    public Page<PostReadResponse> getPostList(Pageable pageable, @RequestParam(required = false) String keyword) {
        if (keyword != null) {
            // 검색 키워드가 있을 경우
            searchByTitle(pageable, keyword);
        }
        // 없을 경우 전체 목록 조회
        return PostReadResponse.of(postRepository.findAll(pageable));
    }

    /**
     * 게시물 정보 수정
     *
     * @param userId            현재 로그인된 회원 id
     * @param postId            수정하고자 하는 포스트 id
     * @param postUpdateRequest 수정사항
     * @return 수정 후 게시물 정보 리턴
     */
    @Transactional
    public PostUpdateResponse update(Long postId, String userId, PostUpdateRequest postUpdateRequest) {
        validateUser(userId);
        verifyPostAuthor(userId, postId);
        Post findPost = validatePost(postId);
        findPost.updatePost(postUpdateRequest.getTitle(), postUpdateRequest.getBody());
        Post updatedPost = postRepository.save(findPost);
        return PostUpdateResponse.of(updatedPost);
    }

    /**
     * 게시물 삭제
     *
     * @param postId 삭제하고자 하는 게시물 id
     * @param userId 현재 로그인된 회원 id
     * @return 삭제한 게시물 정보 리턴
     */
    public PostDeleteResponse delete(Long postId, String userId) {
        validatePost(postId);
        log.info("postId: {}", postId);
        validateUser(userId);
        log.info("userId: {}", userId);
        verifyPostAuthor(userId, postId);
        postRepository.deleteById(postId);
        return PostDeleteResponse.of(postId);
    }

    /**
     * 회원 존재 여부 확인
     *
     * @param userId 검증하려는 회원 id
     * @return 회원이 존재할 경우, 해당 회원 id를 가진 회원 리턴. 존재하지 않을 경우, USER_NOT_FOUND 에러 발생
     */
    public User validateUser(String userId) {
        return userRepository.findById(Long.parseLong(userId)).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });
    }

    /**
     * 게시물 존재 여부 확인
     *
     * @param postId 검증하려는 게시물 id
     * @return 게시물이 존재할 경우, 해당 id를 가지는 party 리턴. 존재하지 않을 경우, PARTY_NOT_FOUND 에러 발생
     */
    public Post validatePost(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> {
            throw new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage());
        });
    }

    /**
     * 회원 게시물 작성자 여부 확인
     *
     * @param userId 검증하려는 회원 id
     * @param postId 검증하려는 게시물 id
     *               해당 회원이 해당 게시물의 작성자 여부 확인
     *               회원이 게시물의 작성자가 아닐 경우, INVALID_PERMISSION 에러 발생
     */
    public void verifyPostAuthor(String userId, Long postId) {
        if (userRepository.findById(Long.parseLong(userId)).get().getId() != postRepository.findById(postId).get().getUser().getId()) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }
    }

    /**
     * 조회수 중복 방지를 위한 쿠키 확인
     *
     * @param postId   검증하려는 게시물 id
     * @param request  HTTP 헤더 및 데이터 (현재 로그인 된 회원의 쿠키 정보)
     * @param response HTTP 응답 코드를 지정해주고 헤더와 바디를 생성 (게시글 id를 oldCookie에 전달)
     *                 쿠키값을 정의하고 쿠키값이 있을 경우, 조회수 증가. 쿠키값이 없을 경우, 조회수 증가 로직 미수행
     */
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

    /**
     * 게시물 검색
     *
     * @param pageable 페이징 처리
     * @param keyword  게시물 내용이 포함된 검색 키워드
     *                 검색 조건에 부합하는 게시물의 상세 정보를 페이징해 리턴
     */
    public Page<PostReadResponse> searchByTitle(Pageable pageable, String keyword) {
        if (keyword == null) {
            searchByTitle(pageable, null);
        } else {
            searchByTitle(pageable, keyword);
        }
        return PostReadResponse.of(postRepository.findByTitleContaining(keyword, pageable));
    }

}
