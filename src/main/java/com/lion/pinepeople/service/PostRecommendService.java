package com.lion.pinepeople.service;


import com.lion.pinepeople.domain.dto.postRecommend.PostRecommendRequest;
import com.lion.pinepeople.domain.dto.postRecommend.PostRecommendResponse;
import com.lion.pinepeople.domain.entity.Post;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.PostRecommendRepository;
import com.lion.pinepeople.repository.PostRepository;
import com.lion.pinepeople.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostRecommendService {


    private final PostRecommendRepository postRecommendRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;


    // 좋아요 수 증가하기
    // 좋아요 눌러지고 저장도 되는데 좋아요 취소가 안된다
    public PostRecommendResponse addRecommend(Long postId, String userId, PostRecommendRequest postRecommendRequest) {

        // Null 예외 처리
        Post findPost = validatePost(postId);
        User findUser = validateUser(userId);
//      validateRecommendCount(postId, userId);

//        validateRecommend(postId, userId);


//        verifyRecommendAuthor(postId, userId, postRecommendRequest); // 추천수 검증


//        Optional<PostRecommend> optionalRecommend = postRecommendRepository.findByPostAndUser(findPost, findUser);


        checkRecommendCount(postId, userId);
//        여기 주석
        findPost.addRecommendsCount();




//        PostRecommend savedRecommend = postRecommendRepository.save(postRecommendRequest.of(findUser, findPost));
//
        Integer RecommendsCount = getRecommendsCount(postId);

//      public static PostRecommendResponse of(PostRecommend savedRecommend) {

        return PostRecommendResponse.of(postRecommendRepository.save(postRecommendRequest.of(findUser, findPost)), RecommendsCount);

//        return PostRecommendResponse.of(savedRecommend, RecommendsCount); // 여기1

    }


//    public void verifyRecommendAuthor(Long postId, String userId, PostRecommendRequest postRecommendRequest) {
//
//        // 에러 처리
//        User user = validateUser(userId);
//
//        Optional<PostRecommend> optionalPostRecommend = postRecommendRepository.findByPostIdAndUserId(postId, user.getId());
//
//        if (!(optionalPostRecommend.isPresent())) {
//
//            Post post = validatePost(postId);
//
//            PostRecommend savedRecommend = postRecommendRepository.save(PostRecommend.builder()
//                    .user(user)
//                    .post(post)
//                    .build());
//            post.addRecommendsCount();
//
//
//        } else {
//
//            postRecommendRepository.delete(optionalPostRecommend.get());
//            Post post = validatePost(postId);
//            post.deleteRecommendsCount();
//
//            // 에러 메세지 출력
////            checkRecommendCount(postId, userId);
//
//
//        }
//

//        postRecommendRepository.save(postRecommendRequest.of(user, post));


//        PostRecommend postRecommend = postRecommendRequest.of(post, user);


//        PostRecommend postRecommend = null;
//        Integer count = postRecommend.getRecommendCnt();
//
//        if (!(count.isPresent())) { //        Optional<PostRecommend> postRecommend = postRecommendRepository.findByPostAndUser(post, user);
//            Optional<Integer> count = Optional.ofNullable(postRecommend.getRecommendCnt());
//
//            Integer count = postRecommend.getRecommendCnt();
//
//
//            post.addRecommendsCount(post.getRecommendsCount());
//            plusRecommendCnt(postId);
//
//        } else {
//
//            post.deleteRecommendsCount(post.getRecommendsCount());
////            postRecommendRepository.delete(optionalRecommend.get().getId());
//            minusRecommendCnt(postId);
//        }


//        좋아요 눌러지긴 하는데 좋아요 개수가 안 보인다
//        Optional<PostRecommend> optionalPostRecommend = postRecommendRepository.findByPostIdAndUserId(postId, user.getId());
//
//
//        if (optionalPostRecommend.isEmpty()) {
//
////            Post post = validatePost(postId);
////            validatePost(postId);
//            postRecommendRepository.save(PostRecommend.builder()
//                    .user(user)
//                    .build());
//
//            plusRecommendCnt(postId);
//
//
//        } else {
//
//            postRecommendRepository.delete(optionalPostRecommend.get());
//            minusRecommendCnt(postId);
//
//        }


//        Optional<PostRecommend> optionalRecommend = postRecommendRepository.findByPostAndUser(post, user); //  여기2
//        Optional<Post> optionalPost = postRepository.findById(Long.parseLong(userId));
//

//        Optional<PostRecommend> optionalRecommend =

//        if () {
//            log.info("optionalRecommend.isEmpty(): {}", optionalRecommend.isEmpty());
//        }

//        log.info("optionalRecommend : {}", optionalRecommend);


//
//        // 추천수 검증: 유저 id == 추천에 저장된 유저 id 0이거나
//        // 좋아요를 누르지 않은 사람
//        if (!optionalRecommend.isPresent()) { //|| optionalRecommend.get().getUser().getId() != optionalPost.get().getRecommends().getUser().getId()) {
//
//            // 1) 다르면 -> 추천  ++증가
//            post.addRecommendsCount(post.getRecommendsCount());
////            postRecommendRepository.saveAndFlush(optionalRecommend.get());
//
//            plusRecommendCnt(postId);
//
//        } else {
//
//            // 2) 같으면 -> 추천 --삭제
//            post.deleteRecommendsCount(post.getRecommendsCount());
//            postRecommendRepository.deleteById(optionalRecommend.get().getId());
//            minusRecommendCnt(postId);
//        }
//
//        return post.getRecommendsCount();
//
//    }


//    Optional<Like> optionalLike = likeRepository.findByPostAndUser(findPost, findUser);
//        if (optionalLike.isPresent()) {
    //        Like findLike = optionalLike.get();
    //        likeRepository.delete(findLike);
    //        return "좋아요를 취소했습니다.";
    //    }
    //    //엔티티 생성 후 저장
    //    Like like = Like.of(findUser, findPost);
    //        likeRepository.save(like);
    //    //알람 엔티티 저장
    //        AlarmUtil.saveAlarm(alarmRepository, AlarmType.NEW_LIKE_ON_POST, findUser, findPost);
    //    //likeResponse 리턴
    //        return "좋아요를 눌렀습니다.";
    //}


    @Transactional(readOnly = true)
    public Integer getRecommendsCount(Long postId) {
        Post post = validatePost(postId);
        Integer postRecommendCount = postRecommendRepository.countByPost(post);
        return postRecommendCount;
    }

//    public void plusRecommendCnt(Long postId) {
//
//        Post post = validatePost(postId);
//        post.addRecommendsCount();
//        postRepository.save(post);
////        postRepository.save(optionalRecommend);
//    }
//
//    public void minusRecommendCnt(Long postId) {
//
//        Post post = validatePost(postId);
//        post.deleteRecommendsCount();
//        postRepository.delete(post);
//
//    }


//    public Page<PostRecommendResponse> getMyRecommends(Pageable pageable, String userId) {
//
//        return PostRecommendResponse.of(postRecommendRepository.findByUser(pageable, validateUser(userId)));
//
//    }


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
//
//    public Boolean validateRecommend(Long postId, String userId) {
//        User user = userRepository.findById(Long.parseLong(userId))
//                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
//        return postRecommendRepository.existsByPostIdAndUserId(postId, user.getId());
//    }
//
//
    private void checkRecommendCount(Long postId, String userId) {
        postRecommendRepository.findByPostIdAndUserId(postId, Long.parseLong(userId))
                .ifPresent(entity -> {
                    throw new AppException(ErrorCode.ALREADY_LIKED, ErrorCode.ALREADY_LIKED.getMessage());
                });
    }



}
