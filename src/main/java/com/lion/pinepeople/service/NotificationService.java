package com.lion.pinepeople.service;

import com.lion.pinepeople.domain.dto.notification.NotificationDto;
import com.lion.pinepeople.domain.dto.notification.NotificationReadResponse;
import com.lion.pinepeople.domain.entity.Notification;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.enums.NotificationType;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.EmitterRepository;
import com.lion.pinepeople.repository.EmitterRepositoryImpl;
import com.lion.pinepeople.repository.NotificationRepository;
import com.lion.pinepeople.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000L * 60L; // 1시간
    private final EmitterRepository emitterRepository = new EmitterRepositoryImpl();

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;


    /**
     * 로그인 유저 sse 연결
     * 1. userId에 현재시간을 더해 emitterId,eventId 생성
     * 2. SseEmitter 유효시간 설정하여 생성 (시간이 지나면 자동으로 클라이언트에게 재연결 요청)
     * 3. 유효시간동안 어느 데이터도 전송되지 않을시 503에러 발생 하므로 맨처음 연결시 더미데이터 전송 - 적용 전
     * 4. 클라이언트가 미수신한 Event 목록이 존재할 경우 전송 - 적용 전
     * @return 생성한 SseEmitter
     */
    public SseEmitter subscribe(Long userId, String lastEventId) throws IOException {

        //emitter 하나하나 에 고유의 값을 주기 위해
        String emitterId = makeTimeIncludeId(userId);
        log.info("emitterId = {}", emitterId);

        // 생성된 emitterId를 기반으로 emitter를 저장
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        //시간 만료된 경우 자동으로 레포지토리에서 삭제 처리하는 콜백 등록
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));
        emitter.onError((e)-> emitterRepository.deleteById(emitterId));

        // 503 에러를 방지하기 위해 처음 연결 진행 시 더미 데이터를 전달
        emitter.send(SseEmitter.event().name("connect!!"));

        // 수 많은 이벤트 들을 구분하기 위해 이벤트 ID에 시간을 통해 구분을 해줌
//        sendNotification(emitter,  emitterId, "EventStream Created. [userId=" + userId + "]");

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실 예방
//        if (!lastEventId.isEmpty()) {
//            sendLostData(lastEventId, userId, emitterId, emitter);
//        }
        return emitter;
    }

    // SseEmitter를 구분 -> 구분자로 시간을 사용함 ,
    // 시간을 붙여주는 이유 -> 브라우저에서 여러개의 구독을 진행 시
    // 탭 마다 SssEmitter 구분을 위해 시간을 붙여 구분하기 위해 아래와 같이 진행
    // 데이터가 유실된시점을 파악하기 위해 userId에 현재시간을 더한다.
    private String makeTimeIncludeId(Long userId) {
        return userId + "_" + System.currentTimeMillis();
    }

    // 유효시간동안 어느 데이터도 전송되지 않으면 503 에러가 발생하기 때문에 더미데이터를 발행
    private void sendNotification(SseEmitter emitter, String eventId, NotificationDto notificationDto) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .name("sse")
                    .data(notificationDto, MediaType.APPLICATION_JSON));
        } catch (IOException e) {
            emitterRepository.deleteById(eventId);
            log.error("SSE 연결 오류!", e);
        }
    }

    // 로그인후 sse연결요청시 헤더에 lastEventId가 있다면, 저장된 데이터 캐시에서 id값을 통해 유실된 데이터만 다시 전송
    // 받지못한 데이터가 있다면 last - event - id를 기준으로 그 뒤의 데이터를 추출해 알림을 보내주면 된다.
//    private void sendLostData(String lastEventId, Long userId, String emitterId, SseEmitter emitter) {
//        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByUserId(String.valueOf(userId));
//        eventCaches.entrySet().stream()
//                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
//                .forEach(entry -> sendNotification(emitter,  emitterId, entry.getValue()));
//    }
    /**
     * 알림 송신
     * 1. 알림생성 & 저장
     * 2. 수신자의 emitter들을 모두찾아 해당 emitter로 송신
     * @param receiver 수신자
     * @param alarmType 알림 메시지
     */
    @Transactional
    public void send(User receiver, String url, NotificationType alarmType, String content) {
        // 알림 생성 & 저장
        Notification notification = createNotification(receiver, alarmType, content, url);
        System.out.println("1.알람 보내기 요청이 들어옴");

        log.info("content={}",content);
        log.info("notification send id ={}",receiver.getId());
        notificationRepository.save(notification);
        System.out.println("2.알람 저장됨");

        String receiverId = String.valueOf(receiver.getId());
        //emitter 하나하나에 고유의 값을 주기 위해
        String eventId = makeTimeIncludeId(Long.parseLong(receiverId));
        //수신자의 SseEmitter 가져오기
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByUserId(receiverId);

        log.info("emitters.toString()={}",emitters.toString());
        emitters.forEach(
                (key, emitter) -> {
                    //데이터 캐시 저장(유실된 데이터처리하기위함)
//                    emitterRepository.saveEventCache(key, notification);
                    //데이터 전송
//                    sendNotification(emitter, eventId, NotificationDto.from(notification));
                    sendNotification(emitter, receiverId, NotificationDto.from(notification));
                });
        log.info("3.저장완료");
    }

    private Notification createNotification(User receiver, NotificationType notificationType,String content, String url) {
        return Notification.builder()
                .receiver(receiver)
                .notificationType(notificationType)
                .content(content)
                .isRead(false)
                .url(url)
                .build();
    }

    /**
     * 알람 전체 조회
     * @param userId
     * @param pageable
     * @return
     */
    @Transactional
    public Page<NotificationDto> findAllNotifications(Long userId, Pageable pageable) {
        // 유저 존재 여부 확인
        User user = getUser(userId);
        log.info("user.getName()={}",user.getName());
        Page<Notification> notificationPage = notificationRepository.findAllByUserId(user.getId(), pageable);
        Page<NotificationDto> dtoPage = NotificationDto.toDtoList(notificationPage);
        return dtoPage;
    }

    @Transactional(readOnly = true)
    public Integer countUnReadNotifications(Long userId) {
        User user = getUser(userId);
        //유저의 알람리스트에서 ->isRead(false)인 갯수를 측정 ,
        Integer count = notificationRepository.countUnReadNotifications(user.getId());
        return count;
    }

    /**
     * 알람 단건 조회
     * @param notificationId 본인의 알림만 읽기 권한 가짐
     * @param userId
     * @return
     */
    @Transactional
    public NotificationReadResponse findNotification(Long notificationId, Long userId) {
        User user = getUser(userId);
            Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new AppException(ErrorCode.ALARM_NOT_FOUND));
        if (user.getId() != notification.getReceiver().getId()) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, "알림을 읽을 수 있는 권한이 없습니다.");
        }
        readNotification(notificationId);
        return NotificationReadResponse.from(notification);
    }

    @Transactional
    public void readNotification(Long id) {
        Notification notification = notificationRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.ALARM_NOT_FOUND));
        notification.read();
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }
}
