package com.lion.pinepeople.service;

import com.lion.pinepeople.domain.dto.AlarmResponse;
import com.lion.pinepeople.domain.entity.Alarm;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.AlarmRepository;
import com.lion.pinepeople.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;

    public Page<AlarmResponse> findAlarmList(Pageable pageable, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, "존재하지 않는 유저입니다."));

        Page<Alarm> alarmList = alarmRepository.findAllByUser(user, pageable);
        return AlarmResponse.toDtoList(alarmList);
    }
}
