package com.lion.pinepeople.repository;

import com.lion.pinepeople.domain.entity.Post;
import com.lion.pinepeople.domain.entity.PostRecommend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRecommendRepository extends JpaRepository<PostRecommend, Long> {

    Optional<PostRecommend> findByPostIdAndUserId(Long postId, Long userId);

    @Query(value = "SELECT COUNT(*) FROM PostRecommend e WHERE e.post = :post")
    int countByPost(@Param("post") Post post) ;

    void delete(PostRecommend findPostRecommend);

}

