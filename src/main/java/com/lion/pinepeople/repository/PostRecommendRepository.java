package com.lion.pinepeople.repository;

import com.lion.pinepeople.domain.entity.Post;
import com.lion.pinepeople.domain.entity.PostRecommend;
import com.lion.pinepeople.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRecommendRepository extends JpaRepository<PostRecommend, Long> {

    Page<PostRecommend> findByUser(Pageable pageable, User user);

    Boolean existsByPostIdAndUserId(Long postId, Long userId);
    Optional<PostRecommend> findByPostIdAndUserId(Long postId, Long userId);

    Optional<PostRecommend> findByPostAndUser(Post post, User user);



    @Query(value = "SELECT COUNT(*) FROM PostRecommend postRecommend where postRecommend.post = :post", nativeQuery = true)
    int countBypostId(@Param("post") Long postId);


//    @Query(value = "SELECT COUNT(*) FROM Post_Recommend e WHERE e.post = :post and  e.user = :user")

    @Query(value = "SELECT COUNT(*) FROM PostRecommend e WHERE e.post = :post")
    int countByPost(@Param("post") Post post) ;


    // 좋아요 유무 (이미 클릭했을 시 1, 클릭 안 했다면 0)
//    @Query(value = "SELECT COUNT(*) FROM Post_Recommend e WHERE e.post = :post and  e.user = :user")
    Integer countByPostAndUser(@Param("post") Post post, @Param("user") User user);


    void delete(PostRecommend findPostRecommend);



}

