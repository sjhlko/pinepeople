package com.lion.pinepeople.repository;

import com.lion.pinepeople.domain.entity.Post;
import com.lion.pinepeople.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByUser (Pageable pageable, User user);

    // 조회수 순으로 정렬
//    @Modifying
//    @Query("update Post post set post.hits = post.hits + 1 where post.id = :id")
//    void countUpHits(Long id);

//    @Modifying
//    @Query("update Post set hits = hits + 1 where post.post_id = :post_id")
    //int updateHits(@Param(value = "postId") Long postId);


    // 검색
    Page<Post> findByTitle(String keyword, Pageable pageable);
    Page<Post> findByKeywordContaining(String keyword, Pageable pageable);




}