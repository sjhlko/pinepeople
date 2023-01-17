package com.lion.pinepeople.controller;

import com.lion.pinepeople.domain.dto.BrixRequest;
import com.lion.pinepeople.domain.response.Response;
import com.lion.pinepeople.service.BrixService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users/{userId}/brix")
@RequiredArgsConstructor
public class BrixController {
    private final BrixService brixService;
    @PostMapping
    public Response<Void> calculationBrix(@RequestParam Long userId, @RequestBody BrixRequest request, Authentication authentication){
        Long loginUserId = Long.parseLong(authentication.getName());
        String result = brixService.calculationBrix(request, userId, loginUserId);
        return Response.success(result);
    }
}
