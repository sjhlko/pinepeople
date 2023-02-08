package com.lion.pinepeople.repository;

import com.lion.pinepeople.domain.entity.Post;
import com.lion.pinepeople.domain.entity.PostBookmark;
import com.lion.pinepeople.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostBookmarkRepository {

    Page<PostBookmark> findByUser (Pageable pageable, User user);
    Optional<PostBookmark> findByPostAndUser(Post post, User user);

    @Query(value = "SELECT COUNT(*) FROM post_like c where c.post_id = :post_id", nativeQuery = true)
    Long countBookmarkedPost(@Param("post_id") Long postId);

    void delete(PostBookmark findPostBookmark);

}

