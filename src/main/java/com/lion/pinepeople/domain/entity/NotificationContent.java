package com.lion.pinepeople.domain.entity;

import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor
public class NotificationContent {
    private static final int Max_LENGTH = 50;

    @Column(nullable = false,length = Max_LENGTH)
    private String content;

    public NotificationContent(String content){
        if(isNotValidNotificationContent(content)){
            throw new AppException(ErrorCode.INVALID_PERMISSION);
        }
        this.content = content;
    }

    private boolean isNotValidNotificationContent(String content) {
        return Objects.isNull(content) || content.length() > Max_LENGTH || content.isEmpty();
    }
}
