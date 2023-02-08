package com.lion.pinepeople.repository;

import com.lion.pinepeople.domain.entity.Post;
import com.lion.pinepeople.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByUser (Pageable pageable, User user);

    @Modifying
    @Query("update Post post set post.hits = post.hits + 1 where post.id = :id")
    Long countUpHits(Long id);

    // 검색
    Page<Post> findByTitle(String keyword, Pageable pageable);
    Page<Post> findByKeywordContaining(String keyword, Pageable pageable);


}