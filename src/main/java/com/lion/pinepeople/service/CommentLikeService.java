package com.lion.pinepeople.service;


import com.lion.pinepeople.domain.dto.commentLike.CommentLikeReadResponse;
import com.lion.pinepeople.domain.dto.commentLike.CommentLikeRequest;
import com.lion.pinepeople.domain.dto.commentLike.CommentLikeResponse;
import com.lion.pinepeople.domain.entity.Comment;
import com.lion.pinepeople.domain.entity.CommentLike;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.CommentLikeRepository;
import com.lion.pinepeople.repository.CommentRepository;
import com.lion.pinepeople.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {


    private final CommentLikeRepository commentLikeRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public CommentLikeResponse like(Long commentId, Long postId, String userId, CommentLikeRequest commentLikeRequest) {

        validateUser(userId);
        validateComment(commentId, postId);

        // 1. 댓글, 회원 id로 검색 -> 좋아요 id
        Optional<CommentLike> clickedLike = commentLikeRepository.findByCommentAndUser(validateComment(commentId, postId), validateUser(userId));

        // 1-2. 좋아요 취소: 반복 클릭 -> likeId 삭제
        if (clickedLike.isPresent()) {
            CommentLike findCommentLike = clickedLike.get();
            commentLikeRepository.delete(findCommentLike);
        }

        // 2. toEntity(): 좋아요 id DB에 저장
        CommentLike commentLike = commentLikeRequest.of();

        return CommentLikeResponse.of(commentLike);

    }


    public Long countLikedComment(Long postId, Long commentId) {

         validateComment(postId, commentId);

        return commentLikeRepository.countLikedComment(commentId);
    }


    public Page<CommentLikeReadResponse> getMylikes(Pageable pageable, String userId) {

        return CommentLikeReadResponse.of(commentLikeRepository.findByUser(pageable, validateUser(userId)));

    }




    public Comment validateComment(Long postId, Long commentId) {
        return commentRepository.findByPostIdAndId(postId, commentId).orElseThrow(() -> {
            throw new AppException(ErrorCode.COMMENT_NOT_FOUND, ErrorCode.COMMENT_NOT_FOUND.getMessage());
        });
    }


    public User validateUser(String userId) {
        return userRepository.findById(Long.parseLong(userId)).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });
    }


}
