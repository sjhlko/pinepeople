package com.lion.pinepeople.mvc;

import com.lion.pinepeople.domain.dto.notification.NotificationDto;
import com.lion.pinepeople.domain.dto.user.myInfo.MyInfoResponse;
import com.lion.pinepeople.service.NotificationService;
import com.lion.pinepeople.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("pinepeople")
public class NotificationMvcController {

    private final NotificationService notificationService;
    private final UserService userService;

    @GetMapping("/notification/my")
    public String alarmList(@PageableDefault(size = 15, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, Model model, Authentication authentication) {
        MyInfoResponse userInfo = userService.getMyInfo(authentication.getName());
        Page<NotificationDto> notifications = notificationService.findAllNotifications(userInfo.getUserId(), pageable);
        /**페이징 처리**/
        int nowPage = notifications.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, notifications.getTotalPages());

        model.addAttribute("userInfo", userInfo);
        model.addAttribute("notifications", notifications);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "notification/notificationList";
    }
}
