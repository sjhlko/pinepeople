package com.lion.pinepeople.controller;


import com.lion.pinepeople.domain.dto.admin.AdminUpdateRequest;
import com.lion.pinepeople.domain.dto.admin.AllBlackListResponse;
import com.lion.pinepeople.domain.dto.admin.BlackListRequest;
import com.lion.pinepeople.domain.dto.admin.BlackListResponse;
import com.lion.pinepeople.domain.dto.user.role.UserRoleResponse;
import com.lion.pinepeople.domain.response.Response;
import com.lion.pinepeople.service.AdminService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;


@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    private final AdminService adminService;


    /**
     * 계정 등급 변경 메서드
     *
     * @param authentication userId
     * @param id             계정 등급을 변경할 userId
     * @return userName, message
     */
    @PostMapping("/{id}/change-role")
    @ApiOperation(value = "계정 등급 변경")
    public Response<UserRoleResponse> changeRole(@ApiIgnore Authentication authentication, @PathVariable Long id) {
        UserRoleResponse userRoleResponse = adminService.changeRole(authentication.getName(), id);
        return Response.success(userRoleResponse);
    }

    /**
     * 블랙리스트 추가
     * @param request 블랙리스트 추가할 유저 아이디
     * @param authentication 로그인 권한 인증
     * @return 블랙리스트 추가 성공 메세지
     */
    @PostMapping("/black-lists")
    public Response<Void> addBlackList(@RequestBody BlackListRequest request, Authentication authentication){
        String loginUserId = authentication.getName();
        String result = adminService.addBlackList(request, loginUserId);
        return Response.success(result);
    }

    /**
     * 블랙리스트 상태 변경
     * @param blackListId 상태 변경할 블랙리스트 아이디
     * @param authentication 로그인 권한 인증
     * @param request 상태 변경 메세지
     * @return 상태 변경 메세지
     */
    @PatchMapping("/black-lists/{blackListId}")
    public Response<Void> updateBlackList(@PathVariable Long blackListId, Authentication authentication, @RequestBody AdminUpdateRequest request){
        String loginUserId = authentication.getName();
        String result = adminService.updateBlackList(loginUserId,blackListId, request);
        return Response.success(result);
    }

    /**
     * 블랙리스트에서 삭제
     * @param blackListId 삭제할 블랙리스트 아이디
     * @param authentication 로그인 권한 인증
     * @return 삭제 성공 메세지
     */
    @DeleteMapping("/black-lists/{blackListId}")
    public Response<Void> deleteBlackList(@PathVariable Long blackListId, Authentication authentication){
        String loginUserId = authentication.getName();
        String result = adminService.deleteBlackList(blackListId, loginUserId);
        return Response.success(result);
    }

    /**
     * 블랙리스트 상세 조회(아직 에러못고침)
     * @param blackListId 조회할 블랙리스트 아이디
     * @param authentication 로그인 권한 인증
     * @return 블랙리스트 상세 조회 내용(블랙리스트 아이디, 정지 시작 시간, 신고한 유저들)
     */
    @GetMapping("/black-lists/{blackListId}")
    public Response<BlackListResponse> getBlackList(@PathVariable Long blackListId, Authentication authentication){
        String loginUserId = authentication.getName();
        BlackListResponse response = adminService.getBlackList(blackListId, loginUserId);
        return Response.success(response);
    }

    /**
     * 블랙리스트 전체 조회
     * @param authentication 로그인 권한 인증
     * @return 블랙리스트 전체 조회, 페이징포함(블랙리스트 아이디, 정지 시작 시간)
     */
    @GetMapping("/black-lists")
    public Response<Page<AllBlackListResponse>> getAllBlackList(Authentication authentication){
        String loginUserId = authentication.getName();
        PageRequest pageable = PageRequest.of(0,10, Sort.by("blackListId").descending());
        Page<AllBlackListResponse> response = adminService.getAllBlackList(loginUserId, pageable);
        return Response.success(response);
    }
}
