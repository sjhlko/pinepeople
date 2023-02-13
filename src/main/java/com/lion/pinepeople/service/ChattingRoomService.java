package com.lion.pinepeople.service;

import com.lion.pinepeople.domain.entity.ChattingRoom;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.ChattingRoomRepository;
import com.lion.pinepeople.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChattingRoomService {
    private final ChattingRoomRepository chattingRoomRepository;
    private final UserRepository userRepository;

    /**
     * 특정 유저가 존재하는지를 확인함
     * @param userId 존재하는 유저인지 확인하고픈 유저의 id
     * @return 존재할 경우 해당 userId를 가지는 user 리턴, 존재하지 않을 경우 USER_NOT_FOUND 에러 발생
     */
    public User validateUser(String userId){
        return userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));
    }
    public ChattingRoom getChattingRoom(String userId1, Long userId2){
        Optional<ChattingRoom> chattingRoom = chattingRoomRepository.findBySenderAndReceiver(validateUser(userId1),userRepository.findById(userId2).get());
        if(chattingRoom.isPresent()){
            return chattingRoom.get();
        }
        chattingRoom = chattingRoomRepository.findBySenderAndReceiver(userRepository.findById(userId2).get(),validateUser(userId1));
        ChattingRoom newChattingRoom =  chattingRoom.orElseGet(() -> ChattingRoom.builder()
                .sender(validateUser(userId1))
                .receiver(userRepository.findById(userId2).get())
                .build());
        return chattingRoomRepository.save(newChattingRoom);
    }

    public User getChattingUser(Long chattingRoomId, String userId){
        User currentUser = validateUser(userId);
        ChattingRoom chattingRoom = chattingRoomRepository.findById(chattingRoomId)
                .orElseThrow(()->new AppException(ErrorCode.CHATTING_ROOM_NOT_FOUND,ErrorCode.CHATTING_ROOM_NOT_FOUND.getMessage()));
        if (Objects.equals(chattingRoom.getReceiver().getId(), currentUser.getId())){
            return chattingRoom.getSender();
        }
        return chattingRoom.getReceiver();
    }

    public ChattingRoom getChattingRoomById(Long chattingRoomId){
        return chattingRoomRepository.findById(chattingRoomId)
                .orElseThrow(()->new AppException(ErrorCode.CHATTING_ROOM_NOT_FOUND,ErrorCode.CHATTING_ROOM_NOT_FOUND.getMessage()));
    }
}
