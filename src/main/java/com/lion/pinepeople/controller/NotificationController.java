package com.lion.pinepeople.controller;

import com.lion.pinepeople.domain.dto.notification.NotificationDto;
import com.lion.pinepeople.domain.dto.notification.NotificationReadResponse;
import com.lion.pinepeople.domain.dto.notification.NotificationsCountDto;
import com.lion.pinepeople.domain.dto.user.myInfo.MyInfoResponse;
import com.lion.pinepeople.domain.response.Response;
import com.lion.pinepeople.service.NotificationService;
import com.lion.pinepeople.service.UserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@Slf4j
@Api(tags = "Notification API")
@RequestMapping("pinepeople/api")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;


    // MIME TYPE - text/event-stream 형태로 받아야함.
    // 클라이어트로부터 오는 알림 구독 요청을 받는다.
    // 로그인한 유저는 SSE 연결
    // lAST_EVENT_ID = 이전에 받지 못한 이벤트가 존재하는 경우 [ SSE 시간 만료 혹은 종료 ] - 아직 적용 전
    // 전달받은 마지막 ID 값을 넘겨 그 이후의 데이터[ 받지 못한 데이터 ]부터 받을 수 있게 한다
    @GetMapping(value ="/subscribe" , produces = "text/event-stream")
    public SseEmitter subscribe( Authentication authentication,
                                @RequestHeader(value= "Last-Event-ID", required = false, defaultValue = "") String lastEventId){
        log.info("구독 요청 들어옴");
        MyInfoResponse myInfo = userService.getMyInfo(authentication.getName());
        return notificationService.subscribe(myInfo.getUserId(),lastEventId);
    }

    /**
     *
     * @param pageable
     * @param authentication
     * @return
     */
    @GetMapping("/notifications")
    public Response<Page<NotificationDto>> findAll(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, Authentication authentication) {
        String name = authentication.getName();
        log.info("name={}",name);
        Page<NotificationDto> findAll = notificationService.findAllNotifications(Long.parseLong(authentication.getName()), pageable);
        return Response.success(findAll);
    }

    // 알람 단건조회
    @GetMapping("/notifications/{notificationId}")
    public Response<NotificationReadResponse> findOne(@PathVariable Long notificationId,Authentication authentication) {
        String name = authentication.getName();
        log.info("name={}",name);
        NotificationReadResponse notification = notificationService.findNotification(notificationId, Long.parseLong(authentication.getName()));
        return Response.success(notification);
    }

//    //알림 조회 - 현재 읽지않은 알림 갯수
//    @GetMapping(value = "/notifications/count")
//    public Response countUnReadNotifications(Authentication authentication) {
//        Integer integer = notificationService.countUnReadNotifications(Long.parseLong(authentication.getName()));
//        return Response.success(integer);
//    }

}
