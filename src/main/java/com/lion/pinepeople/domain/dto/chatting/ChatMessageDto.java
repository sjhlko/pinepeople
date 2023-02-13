package com.lion.pinepeople.domain.dto.chatting;

import com.lion.pinepeople.domain.entity.Chat;
import com.lion.pinepeople.domain.entity.ChattingRoom;
import com.lion.pinepeople.domain.entity.Participant;
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
    private User user;
    private String message;
    private ChattingRoom chattingRoom;
    public Chat toEntity(){
        return Chat.builder()
                .chattingRoom(this.chattingRoom)
                .message(this.message)
                .sender(this.user)
                .build();
    }
    public static ChatMessageDto of(Chat chat){
        return ChatMessageDto.builder()
                .chattingRoom(chat.getChattingRoom())
                .message(chat.getMessage())
                .user(chat.getSender())
                .build();
    }
}
