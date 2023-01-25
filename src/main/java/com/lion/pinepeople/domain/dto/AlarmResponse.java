package com.lion.pinepeople.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lion.pinepeople.domain.entity.Alarm;
import com.lion.pinepeople.domain.entity.AlarmType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.sql.Timestamp;

@Getter
public class AlarmResponse {

    private Long id;
    private AlarmType alarmType;
    private Long fromUserId;
    private Long targetId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp createdAt;

    @Builder
    public AlarmResponse(Long id, AlarmType alarmType, Long fromUserId, Long targetId, Timestamp createdAt) {
        this.id = id;
        this.alarmType = alarmType;
        this.fromUserId = fromUserId;
        this.targetId = targetId;
        this.createdAt = createdAt;
    }

    public static Page<AlarmResponse> toDtoList(Page<Alarm> alarmList) {
        Page<AlarmResponse> alarmDtoList = alarmList.map(a -> AlarmResponse.builder()
                .id(a.getId())
                .alarmType(a.getAlarmType())
                .fromUserId(a.getFromUserId())
                .targetId(a.getTargetId())
                .createdAt(a.getCreatedAt())
                .build());
        return alarmDtoList;
    }
}
