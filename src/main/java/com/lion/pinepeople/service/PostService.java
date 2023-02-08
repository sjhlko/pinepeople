package com.lion.pinepeople.service;


import com.lion.pinepeople.domain.dto.post.*;
import com.lion.pinepeople.domain.entity.Comment;
import com.lion.pinepeople.domain.entity.Post;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.PostRepository;
import com.lion.pinepeople.repository.UserRepository;
import com.lion.pinepeople.utils.AppUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public PostCreateResponse create(PostCreateRequest postCreateRequest, String userId) {

        validateUser(userId);

        Post savedPost = postRepository.save(postCreateRequest.of());

        return PostCreateResponse.of(savedPost);
    }


    public PostReadResponse getPost(Long postId) {

        validatePost(postId);
        countUpHits(postId);

        return PostReadResponse.of(validatePost(postId));

    }


    public Page<PostReadResponse> getPostList(Pageable pageable) {

        return PostReadResponse.of(postRepository.findAll(pageable));

    }


    public Page<PostReadResponse> getMyPosts(Pageable pageable, String userId) {

        return PostReadResponse.of(postRepository.findByUser(pageable, validateUser(userId)));

    }


    @Transactional
    public PostUpdateResponse update(Long postId, String userId, PostUpdateRequest postUpdateRequest) {

        validatePost(postId);
        validateUser(userId);
        AppUtil.verifyPostAuthor(userId, postId);

        Post updatedPost = validatePost(postId);

        updatedPost.updatePost(postUpdateRequest.getTitle(), postUpdateRequest.getBody());


        return PostUpdateResponse.of(updatedPost);
    }


    public PostDeleteResponse delete(Long postId, String userId) {

        validatePost(postId);
        validateUser(userId);
        AppUtil.verifyPostAuthor(userId, postId);

        postRepository.delete(validatePost(postId));

        return PostDeleteResponse.of(validatePost(postId));

    }


    // 키워드로 게시물 검색
    public Page<PostReadResponse> searchByKeyword(Pageable pageable, String keyword) {


        if (keyword == null) {
            searchByKeyword(pageable, null);
        } else {
            searchByKeyword(pageable, keyword);
        }

        return PostReadResponse.of(postRepository.findByKeywordContaining(keyword, pageable));


    }


    // 제목으로 게시물 검색
    public Page<PostReadResponse> searchByTitle(Pageable pageable, String keyword) {


        if (keyword == null) {
            searchByTitle(pageable, null);
        } else {
            searchByTitle(pageable, keyword);
        }

        return PostReadResponse.of(postRepository.findByTitle(keyword, pageable));

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


    public Long countUpHits(Long id) {
        return postRepository.countUpHits(id);
    }


}