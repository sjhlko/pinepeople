package com.lion.pinepeople.service;


import com.lion.pinepeople.domain.dto.postBookmark.PostBookmarkReadResponse;
import com.lion.pinepeople.domain.dto.postBookmark.PostBookmarkRequest;
import com.lion.pinepeople.domain.entity.Post;
import com.lion.pinepeople.domain.entity.PostBookmark;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.PostBookmarkRepository;
import com.lion.pinepeople.repository.PostRepository;
import com.lion.pinepeople.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostBookmarkService {


    private final PostBookmarkRepository postBookmarkRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;


    public PostBookmarkReadResponse bookmark(Long postId, String userId, PostBookmarkRequest postBookmarkRequest) {

        Optional<PostBookmark> clickedBookmark = postBookmarkRepository.findByPostAndUser(validatePost(postId), validateUser(userId));

        if (clickedBookmark.isPresent()) {
            PostBookmark findPostBookmark = clickedBookmark.get();
            postBookmarkRepository.delete(findPostBookmark);
        }

        PostBookmark postBookmark = postBookmarkRequest.of();

        return PostBookmarkReadResponse.of(postBookmark);

    }


    public Long countBookmark(Long postId) {

        validatePost(postId);
        return postBookmarkRepository.countBookmarkedPost(postId);

    }


    public Page<PostBookmarkReadResponse> getMyBookmarks(Pageable pageable, String userId) {

        return PostBookmarkReadResponse.of(postBookmarkRepository.findByUser(pageable, validateUser(userId)));
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



}
