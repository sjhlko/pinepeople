package com.lion.pinepeople.domain.dto.chatting;

import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.enums.MessageType;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ChatMessageDto {
    private MessageType type;
    private User user;
    private String message;
    private Long chattingRoomId;
}
