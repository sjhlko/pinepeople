package com.lion.pinepeople.utils;


import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.CommentRepository;
import com.lion.pinepeople.repository.PostRepository;
import com.lion.pinepeople.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class AppUtil {

    private static UserRepository userRepository;
    private static PostRepository postRepository;
    private static CommentRepository commentRepository;

    // 게시물 작성 본인 여부 확인
    public static void verifyPostAuthor(String userId, Long postId) {
        if (userRepository.findById(Long.parseLong(userId)).get().getId() != postRepository.findById(postId).get().getUser().getId()) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }
    }


    // 댓글 작성 본인 여부 확인
    public static void verifyCommentAuthor(String userId, Long commentId) {
        if (userRepository.findById(Long.parseLong(userId)).get().getId() != commentRepository.findById(commentId).get().getUser().getId()) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }
    }

}