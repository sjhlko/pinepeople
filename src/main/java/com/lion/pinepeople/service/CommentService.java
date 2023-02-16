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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    /**
     * 게시물에 새 댓글 작성
     *
     * @param userId               로그인한 회원 id
     * @param postId               게시물 id
     * @param commentCreateRequest 입력할 댓글 내용
     * @return 생성된 댓글의 정보 리턴
     */
    @Transactional
    public CommentCreateResponse createComment(String userId, Long postId, CommentCreateRequest commentCreateRequest) {

        Post post = validatePost(postId);
        post.addCommentsCount(post.getCommentsCount());
        postRepository.save(post);
        Comment savedComment = commentRepository.save(commentCreateRequest.of(validateUser(userId), post));
        return CommentCreateResponse.of(savedComment);
    }

    /**
     * 게시물 댓글 페이징 조회
     *
     * @param pageable 페이징
     * @param postId   해당 게시물 id
     * @return 해당 게시물에 페이징 처리된 댓글 리턴
     */
    public Page<CommentReadResponse> readCommentPage(Pageable pageable, Long postId) {
        Page<Comment> comments = commentRepository.findByPost(pageable, validatePost(postId));
        return CommentReadResponse.of(comments);
    }

    /**
     * 게시물 댓글 수정
     *
     * @param userId    로그인한 회원 id
     * @param postId    해당 게시물 id
     * @param commentId 수정할 댓글 id
     * @param body      수정할 댓글 내용
     * @return 수정된 댓글의 정보 리턴
     */
    @Transactional
    public CommentUpdateResponse updateComment(String userId, Long postId, Long commentId, String body) {
        validateUser(userId);
        validateComment(postId, commentId);
        verifyCommentAuthor(userId, commentId);
        Comment updatedComment = validateComment(postId, commentId);
        updatedComment.updateComment(body);
        return CommentUpdateResponse.of(updatedComment);
    }

    /**
     * 게시물 댓글 삭제
     *
     * @param userId    로그인한 회원 id
     * @param postId    해당 게시물 id
     * @param commentId 삭제할 댓글 id
     * @return 삭제된 댓글의 정보 리턴
     */
    @Transactional
    public CommentDeleteResponse deleteComment(String userId, Long postId, Long commentId) {
        log.info("userId: {}", userId);
        validateUser(userId);

        log.info("postId: {}", postId);
        log.info("commentId: {}", commentId);
        validateComment(postId, commentId);
        verifyCommentAuthor(userId, commentId);

        Post post = validatePost(postId);
        post.deleteCommentsCount(post.getCommentsCount());

        commentRepository.deleteById(commentId);

        return CommentDeleteResponse.of(commentId);
    }

    /**
     * 회원 존재 여부 확인
     *
     * @param userId 검증하려는 회원 id
     * @return 회원이 존재할 경우, 해당 회원 id를 가진 회원 리턴. 존재하지 않을 경우, USER_NOT_FOUND 에러 발생
     */
    public User validateUser(String userId) {
        return userRepository.findById(Long.parseLong(userId)).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });
    }

    /**
     * 게시물 존재 여부 확인
     *
     * @param postId 검증하려는 게시물 id
     * @return 게시물이 존재할 경우, 해당 id를 가지는 party 리턴. 존재하지 않을 경우, PARTY_NOT_FOUND 에러 발생
     */
    public Post validatePost(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> {
            throw new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage());
        });
    }

    /**
     * 댓글 존재 여부 확인
     *
     * @param postId    검증하려는 회원 id
     * @param commentId 검증하려는 댓글 id
     *                  해당 게시물에 존재하는 댓글 확인
     * @return 댓글이 존재할 경우, 해당 회원 id를 가진 회원 리턴. 존재하지 않을 경우, COMMENT_NOT_FOUND 에러 발생
     */
    public Comment validateComment(Long postId, Long commentId) {
        return commentRepository.findByPostIdAndId(postId, commentId).orElseThrow(() -> {
            throw new AppException(ErrorCode.COMMENT_NOT_FOUND, ErrorCode.COMMENT_NOT_FOUND.getMessage());
        });
    }

    /**
     * 회원 댓글 작성자 여부 확인
     *
     * @param userId    검증하려는 회원 id
     * @param commentId 검증하려는 댓글 id
     *                  해당 회원이 해당 댓글의 작성자 여부 확인 후, 회원이 댓글의 작성자가 아닐 경우, INVALID_PERMISSION 에러 발생
     */
    public void verifyCommentAuthor(String userId, Long commentId) {
        if (userRepository.findById(Long.parseLong(userId)).get().getId() != commentRepository.findById(commentId).get().getUser().getId()) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }
    }

}