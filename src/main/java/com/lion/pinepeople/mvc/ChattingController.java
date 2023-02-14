//package com.lion.pinepeople.mvc;
//
//import com.lion.pinepeople.domain.dto.chatting.ChatMessageDto;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.servlet.ModelAndView;
//
//@Controller
//public class ChattingController {
//    @MessageMapping("/chat.sendMessage")
//    @SendTo("/topic/public")
//    public ChatMessageDto sendMessage(@Payload ChatMessageDto chatMessage) {
//        return chatMessage;
//    }
//
//    @MessageMapping("/chat.addUser")
//    @SendTo("/topic/public")
//    public ChatMessageDto addUser(@Payload ChatMessageDto chatMessage, SimpMessageHeaderAccessor headerAccessor){
//        System.out.println(chatMessage.getContent());
//        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
//        return chatMessage;
//    }
////    @RequestMapping("/chat")
////    public ModelAndView chat() {
////        ModelAndView mv = new ModelAndView();
////        mv.setViewName("/chatting/chat");
////        return mv;
////    }
//}
