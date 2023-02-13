package com.lion.pinepeople.domain.dto.chatting;

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
    private String content;
    private String sender;
}
