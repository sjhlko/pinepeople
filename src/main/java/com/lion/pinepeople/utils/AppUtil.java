package com.lion.pinepeople.utils;


import com.lion.pinepeople.domain.entity.Comment;
import com.lion.pinepeople.domain.entity.Post;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.CommentRepository;
import com.lion.pinepeople.repository.PostRepository;
import com.lion.pinepeople.repository.UserRepository;

public class AppUtil {

    /****
     * findUser 회원 이름 확인
     * @param userRepository
     * @param id
     * @return
     */
    public static User findUser(UserRepository userRepository, Long id) {
        return userRepository.findById(id).orElseThrow(() -> {
            throw new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage());
        });
    }

    /***
     * findPost 게시물 확인
     * @param postRepository
     * @param id
     * @return
     */
    public static Post findPost(PostRepository postRepository, Long id) {
        return postRepository.findById(id).orElseThrow(() -> {
            throw new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage());
        });

    }


    // 게시물 작성 본인 여부 확인
    public static void checkUser(Long checkUserId, Long id) {
        if (!checkUserId.equals(id)) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }
    }

    // 댓글 확인
    public static Comment findBody(CommentRepository commentRepository, Long postId, Long id) {
        return commentRepository.findByPostIdAndId(postId, id).orElseThrow(() -> {
            throw new AppException(ErrorCode.COMMENT_NOT_FOUND, ErrorCode.COMMENT_NOT_FOUND.getMessage());
        });
    }

}