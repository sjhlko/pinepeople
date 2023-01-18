package com.lion.pinepeople.controller;

import com.lion.pinepeople.domain.response.Response;
import com.lion.pinepeople.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    /**
     *
     * @param userId 신고 당할 유저
     * @param authentication 신고할 유저
     * @return 신고 성공 메세지
     */
    @PostMapping("/users/{userId}/reports")
    public Response<Void> reported(@RequestParam Long userId, Authentication authentication){
        Long loginUserId = Long.parseLong(authentication.getName());
        String result = reportService.addReport(loginUserId, userId);
        return Response.success(result);
    }
}
