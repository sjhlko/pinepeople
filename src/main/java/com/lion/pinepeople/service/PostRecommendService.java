package com.lion.pinepeople.service;


import com.lion.pinepeople.domain.dto.postRecommend.PostRecommendRequest;
import com.lion.pinepeople.domain.dto.postRecommend.PostRecommendResponse;
import com.lion.pinepeople.domain.entity.Comment;
import com.lion.pinepeople.domain.entity.Post;
import com.lion.pinepeople.domain.entity.PostRecommend;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.PostRecommendRepository;
import com.lion.pinepeople.repository.PostRepository;
import com.lion.pinepeople.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostRecommendService {


    private final PostRecommendRepository postRecommendRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;


    public PostRecommendResponse recommend(Long postId, String userId, PostRecommendRequest postRecommendRequest) {

        Post post = validatePost(postId);
        User user = validateUser(userId);
        validateRecommend(post, user);

//        postRecommendRepository.countPostRecommendsBypostId(postId);
        post.addRecommendsCount(post.getRecommendsCount());

        PostRecommend savedRecommend = postRecommendRepository.save(postRecommendRequest.of(post, user));


        return PostRecommendResponse.of(savedRecommend,getRecommendsCount(postId));

    }



//    public Long countRecommend(Long postId) {
//
//        validatePost(postId);
//        return postRecommendRepository.countBookmarkedPost(postId);
//
//    }

//
//    public Page<PostRecommendResponse> getMyRecommends(Pageable pageable, String userId) {
//
//        return PostRecommendResponse.of(postRecommendRepository.findByUser(pageable, validateUser(userId)));
//
//    }
//


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

    private void validateRecommend(Post post, User user) {
        postRecommendRepository.findByPostAndUser(post, user)
                .ifPresent(entity -> {
                    throw new AppException(ErrorCode.ALREADY_LIKED, ErrorCode.ALREADY_LIKED.getMessage());
                });
    }

    public int getRecommendsCount(Long postId) {
        Post post = validatePost(postId);
        int postRecommendsCount = postRecommendRepository.countByPost(post);
        return postRecommendsCount;
    }



}
