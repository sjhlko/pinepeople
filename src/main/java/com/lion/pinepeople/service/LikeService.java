package com.lion.pinepeople.service;

import com.lion.pinepeople.domain.dto.like.LikeResponse;
import com.lion.pinepeople.domain.entity.Like;
import com.lion.pinepeople.domain.entity.Post;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.repository.LikeRepository;
import com.lion.pinepeople.repository.PostRepository;
import com.lion.pinepeople.repository.UserRepository;
import com.lion.pinepeople.utils.AppUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;


    /***
     * like 좋아요
     * @param id
     * @param postId
     * @return
     */
    public LikeResponse like(String id, Long postId) { // ???

        // 회원 id 확인
        User findUser = AppUtil.findUser(userRepository, Long.parseLong(id)); // likeId 형변환

        // 게시물 id 확인
        Post findPost = AppUtil.findPost(postRepository, postId);

        // 좋아요 처리
        Optional<Like> optionalLike = likeRepository.findByPostAndUser(findPost, findUser);

        // 좋아요 취소
        if (optionalLike.isPresent()) {

            Like findLike = optionalLike.get();
            likeRepository.delete(findLike);

          return LikeResponse.builder()
                  .message("좋아요를 취소했습니다")
                  .postId(postId)
                  .build();
        }

        // 좋아요 저장
        Like like = Like.convertToEntity(findUser, findPost);
        likeRepository.save(like);

        return LikeResponse.builder()
                .message("좋아요를 눌렀습니다")
                .postId(postId)
                .build();
    }



    /***
     * countLike 좋아요 개수
     * @param postId
     * @return
     */
    public Long countLike(Long postId) {

        // 게시물 확인
        Post findPost = AppUtil.findPost(postRepository, postId);

        // 좋아요 개수 확인
        Long countTotalLikes = likeRepository.countByPostId(postId);


        // 좋아요 개수 반환
        //return findPost.getLikes().stream().count();
        return countTotalLikes;
    }

}
