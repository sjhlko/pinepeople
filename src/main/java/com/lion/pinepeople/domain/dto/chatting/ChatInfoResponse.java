package com.lion.pinepeople.domain.dto.chatting;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lion.pinepeople.domain.entity.Chat;
import com.lion.pinepeople.domain.entity.ChattingRoom;
import com.lion.pinepeople.domain.entity.User;
import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ChatInfoResponse {
    private Long userId;
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
    private Timestamp createdAt;

    public static ChatInfoResponse of(Chat chat){
        return ChatInfoResponse.builder()
                .message(chat.getMessage())
                .userId(chat.getSender().getId())
                .createdAt(chat.getCreatedAt())
                .build();
    }
}
