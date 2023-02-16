package com.lion.pinepeople.service;


import com.lion.pinepeople.domain.dto.postRecommend.PostRecommendRequest;
import com.lion.pinepeople.domain.dto.postRecommend.PostRecommendResponse;
import com.lion.pinepeople.domain.entity.Post;
import com.lion.pinepeople.domain.entity.PostRecommend;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.enums.NotificationType;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.PostRecommendRepository;
import com.lion.pinepeople.repository.PostRepository;
import com.lion.pinepeople.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostRecommendService {

    private final PostRecommendRepository postRecommendRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final NotificationService notificationService;

//        1. 좋아요 정보를 조회(GET매핑) 할 때 개수 및 자신이 좋아요를 눌렀는지 반환
//        2. 좋아요를 누를 때 (POST매핑) 해당 글이나 댓글 좋아요에 본인 등록 및 좋아요 개수 + 1
//        3. 좋아요를 삭제할 때 (DELETE매핑) 해당 글이나 댓글 좋아요에 본인 삭제 및 좋아요 개수 - 1

    /**
     * 게시물에 추천 추가
     *
     * @param postId               추천을 추가할 게시물 id
     * @param userId               로그인한 회원 id
     * @param postRecommendRequest 추천 정보
     * @return 추가된 게시물 추천 정보 리턴
     */
    @Transactional
    public PostRecommendResponse addRecommend(Long postId, String userId, PostRecommendRequest postRecommendRequest) {

        Post findPost = validatePost(postId);
        User findUser = validateUser(userId);
        verifyRecommendAuthor(postId, userId);
        findPost.addRecommendsCount();

        PostRecommend savedRecommend = postRecommendRepository.save(postRecommendRequest.of(findUser, findPost));

        String url = "/pinepeople/board/" + findPost.getId();
        notificationService.send(findPost.getUser(), url, NotificationType.LIKE_ON_POST,
                findUser.getName() + "님이 " + "\"" + findPost.getTitle() + "\" " + NotificationType.LIKE_ON_POST.getMessage());

        Integer RecommendsCount = getRecommendsCount(postId);
        return PostRecommendResponse.of(postRecommendRepository.save(savedRecommend), RecommendsCount);

    }

    /**
     * 해당 게시물에 존재하는 모든 추천수 조회
     *
     * @param postId 추천개수를 조회할 게시물 id
     * @return 해당 게시물의 추천수를 카운트하여 리턴
     */
    public Integer getRecommendsCount(Long postId) {
        Post post = validatePost(postId);
        Integer postRecommendCount = postRecommendRepository.countByPost(post);
        return postRecommendCount;
    }

    /**
     * 게시물에 추천 삭제
     *
     * @param postId 추천을 삭제하고자 하는 게시물 id
     * @param userId 로그인한 회원 id
     * @return 삭제한 게시물 추천 정보 리턴
     */
    public PostRecommendResponse deleteRecommend(Long postId, String userId, PostRecommendRequest postRecommendRequest) {
        Post findPost = validatePost(postId);
        User findUser = validateUser(userId);
        verifyRecommendAuthor(postId, userId);
        findPost.deleteRecommendsCount();

        PostRecommend savedRecommend = postRecommendRepository.save(postRecommendRequest.of(findUser, findPost));

        Integer RecommendsCount = getRecommendsCount(postId);
        return PostRecommendResponse.of(postRecommendRepository.save(savedRecommend), RecommendsCount);
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
     * 회원 추천 작성자 여부 확인
     *
     * @param userId      검증하려는 회원 id
     * @param recommendId 검증하려는 게시물 id
     *                    해당 회원이 해당 게시물의 작성자 여부 확인
     *                    회원이 게시물의 작성자가 아닐 경우, INVALID_PERMISSION 에러 발생
     */
    private void verifyRecommendAuthor(Long recommendId, String userId) {
        postRecommendRepository.findByPostIdAndUserId(recommendId, Long.parseLong(userId))
                .ifPresent(entity -> {
                    throw new AppException(ErrorCode.ALREADY_LIKED, ErrorCode.ALREADY_LIKED.getMessage());
                });
    }

}