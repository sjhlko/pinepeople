package com.lion.pinepeople.controller;

import com.lion.pinepeople.domain.dto.chatting.ChatInfoResponse;
import com.lion.pinepeople.domain.dto.chatting.ChatMessageDto;
import com.lion.pinepeople.domain.dto.chatting.ChattingRoomDto;
import com.lion.pinepeople.domain.dto.chatting.Room;
import com.lion.pinepeople.domain.entity.Chat;
import com.lion.pinepeople.domain.entity.ChattingRoom;
import com.lion.pinepeople.domain.response.Response;
import com.lion.pinepeople.repository.UserRepository;
import com.lion.pinepeople.service.ChatService;
import com.lion.pinepeople.service.ChattingRoomService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Api(tags = "Chatting API")
@RestController
@RequestMapping("/pinepeople")
@RequiredArgsConstructor
@Slf4j
public class ChattingController {
    private final UserRepository userRepository;
    private final ChattingRoomService chattingRoomService;
    private final ChatService chatService;
    List<Room> roomList = new ArrayList<Room>();
    static int roomNumber = 0;


    /**
     * 방 정보 가져오기
     * @param userId
     * @return
     */
    @PostMapping("/get-room")
    public Response<ChattingRoomDto> createRoom(@RequestParam Long userId, Authentication authentication){
        ChattingRoom chattingRoom = chattingRoomService.getChattingRoom(authentication.getName(), userId);
        return Response.success(ChattingRoomDto.builder().chattingRoomId(chattingRoom.getId()).build());
    }

    /**
     * 채팅 내역 저장하기
     */
    @PostMapping("/chat-save/{chattingRoomId}")
    public Response<String> saveChatting(@RequestBody ChatMessageDto chatMessageDto, @PathVariable Long chattingRoomId, Authentication authentication){
        Chat chat = chatService.saveChat(chatMessageDto);
        return Response.successToMessage("채팅 저장 성공");
    }

    /**
     * 채팅 내역 불러오기
     */
    @GetMapping("/get-chatting/{chattingRoomId}")
    public Response<List<ChatInfoResponse>> saveChatting(@PathVariable Long chattingRoomId){
        List<ChatInfoResponse> chat = chatService.getChat(chattingRoomId);
        return Response.success(chat);
    }
}
