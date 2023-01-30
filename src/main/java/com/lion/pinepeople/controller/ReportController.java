package com.lion.pinepeople.controller;

import com.lion.pinepeople.domain.dto.report.ReportRequest;
import com.lion.pinepeople.domain.response.Response;
import com.lion.pinepeople.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    public Response<Void> reported(@PathVariable Long userId, Authentication authentication, @RequestBody ReportRequest request){
        String loginUserId = authentication.getName();
        String result = reportService.addReport(loginUserId, userId, request);
        return Response.success(result);
    }
}
