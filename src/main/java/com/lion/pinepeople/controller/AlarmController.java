package com.lion.pinepeople.controller;

import com.lion.pinepeople.domain.dto.AlarmResponse;
import com.lion.pinepeople.domain.response.Response;
import com.lion.pinepeople.service.AlarmService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Api(tags = "Alarm API")
public class AlarmController {

    private final AlarmService alarmService;

    @GetMapping("/alarms/my")
    public Response<Page<AlarmResponse>> alarmList(Pageable pageable,@ApiIgnore Authentication authentication) {
        Long loginUserId = Long.parseLong(authentication.getName());
        Page<AlarmResponse> alarmList = alarmService.findAlarmList(pageable, loginUserId);
        return Response.success(alarmList);
    }
}
