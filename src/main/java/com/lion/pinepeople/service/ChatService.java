package com.lion.pinepeople.service;

import com.lion.pinepeople.domain.dto.chatting.ChatInfoResponse;
import com.lion.pinepeople.domain.dto.chatting.ChatMessageDto;
import com.lion.pinepeople.domain.dto.participant.ParticipantInfoResponse;
import com.lion.pinepeople.domain.entity.Chat;
import com.lion.pinepeople.domain.entity.ChattingRoom;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.ChatRepository;
import com.lion.pinepeople.repository.ChattingRoomRepository;
import com.lion.pinepeople.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {
    private final ChatRepository chatRepository;

    private final UserRepository userRepository;
    private final ChattingRoomRepository chattingRoomRepository;

    /**
     * 특정 유저가 존재하는지를 확인함
     * @param userId 존재하는 유저인지 확인하고픈 유저의 id
     * @return 존재할 경우 해당 userId를 가지는 user 리턴, 존재하지 않을 경우 USER_NOT_FOUND 에러 발생
     */
    public User validateUser(String userId){
        return userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));
    }

    public List<ChatInfoResponse> getChat(Long chattingRoomId){
        ChattingRoom chattingRoom = chattingRoomRepository.findById(chattingRoomId)
                .orElseThrow(() -> new AppException(ErrorCode.CHATTING_ROOM_NOT_FOUND, ErrorCode.CHATTING_ROOM_NOT_FOUND.getMessage()));
        List<Chat> chats = chatRepository.findAllByChattingRoom(chattingRoom);
        return chats.stream().map(ChatInfoResponse::of).collect(Collectors.toList());
    }

    public Chat saveChat(ChatMessageDto chatMessageDto){
        return chatRepository.save(chatMessageDto.toEntity());
    }
}
