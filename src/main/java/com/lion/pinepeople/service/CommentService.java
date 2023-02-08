package com.lion.pinepeople.service;

import com.lion.pinepeople.domain.dto.comment.*;
import com.lion.pinepeople.domain.entity.Comment;
import com.lion.pinepeople.domain.entity.Post;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.CommentRepository;
import com.lion.pinepeople.repository.PostRepository;
import com.lion.pinepeople.repository.UserRepository;
import com.lion.pinepeople.utils.AppUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {



    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;



    @Transactional
    public CommentCreateResponse createComment(String userId, Long postId, CommentCreateRequest commentCreateRequest) {

        Comment savedComment = commentRepository.save(commentCreateRequest.of(validateUser(userId), validatePost(postId)));

        return CommentCreateResponse.of(savedComment);
    }



    public Page<CommentReadResponse> readCommentPage(Pageable pageable, Long postId) {

        Page<Comment> comments = commentRepository.findByPost(pageable, validatePost(postId));

        return CommentReadResponse.of(comments);
    }



    public Page<CommentReadResponse> getMyComments(String userId, Pageable pageable) {

        Page<Comment> myComments = commentRepository.findByUser(pageable, validateUser(userId));

        return CommentReadResponse.of(myComments);
    }


    public CommentUpdateResponse updateComment(String userId, Long postId, Long commentId, String body) {

        validateUser(userId);
        validateComment(postId, commentId);
        AppUtil.verifyCommentAuthor(userId, commentId);

        Comment updatedComment = validateComment(postId, commentId);

        updatedComment.updateComment(body);

        return CommentUpdateResponse.of(updatedComment);
    }


    public CommentDeleteResponse deleteComment(String userId, Long postId, Long commentId) {

        validateUser(userId);
        validateComment(postId, commentId);
        AppUtil.verifyCommentAuthor(userId, commentId);

        commentRepository.delete(validateComment(postId, commentId));

        return CommentDeleteResponse.of(commentId);
    }


    public User validateUser(String userId) {
        return userRepository.findById(Long.parseLong(userId)).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });
    }

    public Post validatePost(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> {
            throw new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage());
        });
    }


    public Comment validateComment(Long postId, Long commentId) {
        return commentRepository.findByPostIdAndId(postId, commentId).orElseThrow(() -> {
            throw new AppException(ErrorCode.COMMENT_NOT_FOUND, ErrorCode.COMMENT_NOT_FOUND.getMessage());
        });
    }


}
