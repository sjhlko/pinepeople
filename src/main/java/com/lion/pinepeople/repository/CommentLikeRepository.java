package com.lion.pinepeople.repository;


import com.lion.pinepeople.domain.entity.Comment;
import com.lion.pinepeople.domain.entity.CommentLike;
import com.lion.pinepeople.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    Page<CommentLike> findByUser (Pageable pageable, User user);
    Optional<CommentLike> findByCommentAndUser(Comment comment, User user);


    @Query(value = "SELECT COUNT(*) FROM comment_like c where c.comment_id = :comment_id", nativeQuery = true)
    Long countLikedComment(@Param("comment_id") Long commentId);

}
