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

import java.io.IOException;

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
                                @RequestHeader(value= "Last-Event-ID", required = false, defaultValue = "") String lastEventId) throws IOException {
        log.info("구독 요청 들어옴");
        if (authentication == null) {
            return null;
        }else {
            return notificationService.subscribe(Long.parseLong(authentication.getName()), lastEventId);
        }
    }

    /**
     * 알림 전체 조회
     * @param pageable 페이징 처리
     * @param authentication 로그인한 본인의 알람만 조회가능
     * @return
     */
    @GetMapping("/notifications")
    public Response<Page<NotificationDto>> findAll(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, Authentication authentication) {
        String name = authentication.getName();
        log.info("name={}",name);
        Page<NotificationDto> findAll = notificationService.findAllNotifications(Long.parseLong(authentication.getName()), pageable);
        return Response.success(findAll);
    }


    /**
     * 알람 단건 조회(읽음 처리)
     * @param notificationId 알림 아이디
     * @param authentication 로그인한 본인의 알람만 조회가능
     * @return
     */
    @GetMapping("/notifications/{notificationId}")
    public Response<NotificationReadResponse> findOne(@PathVariable Long notificationId,Authentication authentication) {
        String name = authentication.getName();
        log.info("name={}",name);
        NotificationReadResponse notification = notificationService.findNotification(notificationId, Long.parseLong(authentication.getName()));
        return Response.success(notification);
    }


    /**
     * 읽지 않은 알람의 개수 조회
     * @param authentication 로그인한 본인의 알람만 조회가능
     * @return 읽지 않은 알람 개수 반환
     */
    @GetMapping(value = "/notifications/count")
    public Response countUnReadNotifications(Authentication authentication) {
        if (authentication == null) {
            return null;
        }else {
            Integer integer = notificationService.countUnReadNotifications(Long.parseLong(authentication.getName()));
            return Response.success(integer);
        }
    }

}
