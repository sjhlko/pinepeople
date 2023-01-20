package com.lion.pinepeople.utils;

import com.lion.pinepeople.domain.entity.Post;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.PostRepository;
import com.lion.pinepeople.repository.UserRepository;

public class AppUtil {


    public static User findUser(UserRepository userRepository, Long userId) {
        return userRepository.findByUserId(userId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage());
        });
    }

    public static Post findPost(PostRepository postRepository, Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> {
            throw new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage());
        });

    }



    // 게시물 작성 본인여부 확인
    public static void checkUser(Long checkUserId, Long userId) {
        if (!checkUserId.equals(userId)) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }
    }


}
