package com.lion.pinepeople.repository;

import com.lion.pinepeople.domain.entity.Comment;
import com.lion.pinepeople.domain.entity.Post;
import com.lion.pinepeople.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByPostIdAndId (Long postId, Long id); // 단일 조회
    Page<Comment> findByPost (Pageable pageable, Post post); // 목록 조회
    Page<Comment> findByUser(Pageable pageable, User findUser); // 내 게시물 조회

}