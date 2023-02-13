package com.lion.pinepeople.mvc;

import com.lion.pinepeople.domain.dto.chatting.Room;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("pinepeople")
public class ChattingMainController {
    private final UserRepository userRepository;
    List<Room> roomList = new ArrayList<Room>();
    static int roomNumber = 0;

    @GetMapping("/chat")
    public ModelAndView chat() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("chatting/chat");
        return mv;
    }

    /**
     * 방 페이지
     * @return
     */
    @GetMapping("/room")
    public ModelAndView room(Authentication authentication) {
        ModelAndView mv = new ModelAndView();
        System.out.println(getUser(authentication).getName());
        mv.setViewName("chatting/room");
        return mv;
    }

    /**
     * 방 생성하기
     * @param params
     * @return
     */
    @PostMapping("/createRoom")
    public @ResponseBody List<Room> createRoom(@RequestParam HashMap<Object, Object> params){
        String roomName = (String) params.get("roomName");
        if(roomName != null && !roomName.trim().equals("")) {
            Room room = new Room();
            room.setRoomNumber(++roomNumber);
            room.setRoomName(roomName);
            roomList.add(room);
        }
        return roomList;
    }

    /**
     * 방 정보가져오기
     * @param params
     * @return
     */
    @PostMapping("/getRoom")
    public @ResponseBody List<Room> getRoom(@RequestParam HashMap<Object, Object> params, Authentication authentication){
        System.out.println(getUser(authentication).getName());
        return roomList;
    }

    /**
     * 채팅방
     * @return
     */
    @GetMapping("/moveChating")
    public ModelAndView chating(@RequestParam HashMap<Object, Object> params, Authentication authentication) {
        ModelAndView mv = new ModelAndView();
        int roomNumber = Integer.parseInt((String) params.get("roomNumber"));

        List<Room> new_list = roomList.stream().filter(o->o.getRoomNumber()==roomNumber).collect(Collectors.toList());
        if(new_list != null && new_list.size() > 0) {
            mv.addObject("roomName", params.get("roomName"));
            mv.addObject("roomNumber", params.get("roomNumber"));
            mv.addObject("user",getUser(authentication));
            System.out.println(getUser(authentication).getName());
            mv.setViewName("chatting/chat");
        }else {
            mv.setViewName("chatting/room");
        }
        return mv;
    }

    private User getUser(Authentication authentication) {
        long userId = Long.parseLong(authentication.getName());
        return userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }
}
