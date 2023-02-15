package com.lion.pinepeople.mvc;

import com.lion.pinepeople.domain.entity.ChattingRoom;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.UserRepository;
import com.lion.pinepeople.service.ChattingRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("pinepeople")
public class ChattingMvcController {
    private final UserRepository userRepository;
    private final ChattingRoomService chattingRoomService;

    @GetMapping("/chat")
    public String chat() {
        return "chatting/chat";
    }


    /**
     * 채팅방
     * @return
     */
    @GetMapping("/movechatting")
    public String movechatting(@RequestParam Long roomNumber, Authentication authentication, Model model) {
        User user = chattingRoomService.getChattingUser(roomNumber,authentication.getName());
        ChattingRoom chattingRoom = chattingRoomService.getChattingRoomById(roomNumber);
        model.addAttribute("chattingRoom", chattingRoom);
        model.addAttribute("roomName", user.getName());
        model.addAttribute("roomNumber", roomNumber);
        model.addAttribute("user",getUser(authentication));
        return "chatting/chat";
    }


    private User getUser(Authentication authentication) {
        long userId = Long.parseLong(authentication.getName());
        return userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }
}
