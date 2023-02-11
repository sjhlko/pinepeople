package com.lion.pinepeople.service;


import com.lion.pinepeople.domain.dto.postRecommend.PostRecommendRequest;
import com.lion.pinepeople.domain.dto.postRecommend.PostRecommendResponse;
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
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@ComponentScan(basePackages = {"com.lion.pinepeople.repository.PostRecommendRepository"})
public class PostRecommendService {


    private final PostRecommendRepository postRecommendRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;


    public PostRecommendResponse recommend(Long postId, String userId, PostRecommendRequest postRecommendRequest) {

        log.info("postId: {}", postId);
        log.info("userId: {}", userId);

        Optional<PostRecommend> clickedRecommend = postRecommendRepository.findByPostAndUser(validatePost(postId), validateUser(userId));

        if (clickedRecommend.isPresent()) {
            PostRecommend findPostRecommend = clickedRecommend.get();
            postRecommendRepository.delete(findPostRecommend);
        }

        PostRecommend savedRecommend = postRecommendRepository.save(postRecommendRequest.of(validateUser(userId), validatePost(postId)));

        return PostRecommendResponse.of(savedRecommend);

    }


    public Long countRecommend(Long postId) {

        validatePost(postId);
        return postRecommendRepository.countBookmarkedPost(postId);

    }


    public Page<PostRecommendResponse> getMyRecommends(Pageable pageable, String userId) {

        return PostRecommendResponse.of(postRecommendRepository.findByUser(pageable, validateUser(userId)));

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
