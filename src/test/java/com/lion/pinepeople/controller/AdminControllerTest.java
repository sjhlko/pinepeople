//package com.lion.pinepeople.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.lion.pinepeople.domain.dto.admin.AllBlackListResponse;
//import com.lion.pinepeople.domain.dto.admin.BlackListRequest;
//import com.lion.pinepeople.domain.dto.admin.BlackListResponse;
//import com.lion.pinepeople.domain.dto.user.role.UserRoleResponse;
//import com.lion.pinepeople.domain.entity.BlackList;
//import com.lion.pinepeople.domain.entity.User;
//import com.lion.pinepeople.enums.UserRole;
//import com.lion.pinepeople.exception.ErrorCode;
//import com.lion.pinepeople.exception.customException.AppException;
//import com.lion.pinepeople.service.AdminService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(AdminController.class)
//@MockBean(JpaMetamodelMappingContext.class)
//class AdminControllerTest {
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @MockBean
//    AdminService adminService;
//
//    @Autowired
//    ObjectMapper objectMapper;
//
//    String url1 = "/api/admin/1/change-role";
//    String url2 = "/api/admin/black-lists";
//
//
//
//    @Test
//    @DisplayName("계정 등급 변경")
//    @WithMockUser
//    void change_role_success() throws Exception{
//        UserRoleResponse response = new UserRoleResponse("test","관리자로 권한이 변경되었습니다.");
//        given(adminService.changeRole(any(),any())).willReturn(response);
//
//        mockMvc.perform(post(url1)
//                .with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.result.userName").value(response.getUserName()))
//                .andExpect(jsonPath("$.result.message").value(response.getMessage()))
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("해당 계정이 이미 admin일 경우")
//    @WithMockUser
//    void change_role_fail_admin() throws Exception{
//        given(adminService.changeRole(any(),any())).willThrow(new AppException(ErrorCode.INVALID_PERMISSION, "해당 계정은 ADMIN 계정입니다."));
//
//        mockMvc.perform(post(url1)
//                        .with(csrf()))
//                .andExpect(status().isUnauthorized())
//                .andExpect(jsonPath("$.resultCode").value("ERROR"))
//                .andExpect(jsonPath("$.result.errorCode").value(String.valueOf(ErrorCode.INVALID_PERMISSION)))
//                .andExpect(jsonPath("$.result.message").value("해당 계정은 ADMIN 계정입니다."))
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("유저를 찾을 수 없는 경우")
//    @WithMockUser
//    void change_role_fail_non_userId() throws Exception{
//
//        given(adminService.changeRole(any(),any())).willThrow(new AppException(ErrorCode.USER_NOT_FOUND, "계정 등급을 변경할 유저를 찾을 수 없습니다."));
//
//        mockMvc.perform(post(url1)
//                        .with(csrf()))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.resultCode").value("ERROR"))
//                .andExpect(jsonPath("$.result.errorCode").value(String.valueOf(ErrorCode.USER_NOT_FOUND)))
//                .andExpect(jsonPath("$.result.message").value("계정 등급을 변경할 유저를 찾을 수 없습니다."))
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("블랙리스트 추가 성공")
//    @WithMockUser
//    void addBlackList_success() throws Exception{
//        BlackListRequest request = new BlackListRequest(1L);
//        String response = "블랙리스트 등록 완료하였습니다";
//        given(adminService.addBlackList(any(),any())).willReturn(response);
//
//        mockMvc.perform(post(url2)
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
//                .andExpect(jsonPath("$.result").value(response))
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("블랙리스트 삭제 성공")
//    @WithMockUser
//    void deleteBlackList_success() throws Exception{
//        url2 += "/1";
//        String response = "블랙리스트에서 삭제 완료 하였습니다.";
//        given(adminService.deleteBlackList(any(),any())).willReturn(response);
//
//        mockMvc.perform(delete(url2)
//                        .with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
//                .andExpect(jsonPath("$.result").value(response))
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("블랙리스트 전체 조회 성공")
//    @WithMockUser
//    void getAllBlackList_success() throws Exception{
//        BlackList blackList = BlackList.builder()
//                .blackListId(1L)
//                .startDate(LocalDateTime.now())
//                .build();
//        Page<AllBlackListResponse> response = new PageImpl<>(List.of(AllBlackListResponse.fromEntity(blackList)));
//
//        given(adminService.getAllBlackList(any(),any())).willReturn(response);
//
//        mockMvc.perform(get(url2)
//                        .with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
//                .andExpect(jsonPath("$.result.content[0].blackListId").value(1L))
//                .andExpect(jsonPath("$.result.content[0].startDate").exists())
//                .andExpect(jsonPath("$.result.pageable").exists())
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("블랙리스트 상세 조회 성공")
//    @WithMockUser
//    void getBlackList_success() throws Exception{
//        url2 += "/1";
//        BlackListResponse response = BlackListResponse.builder()
//                .blackListId(1L)
//                .startDate(LocalDateTime.now())
//                .fromUserEmail(List.of("aaa@naver.com"))
//                .build();
//
//        given(adminService.getBlackList(any(),any())).willReturn(response);
//        System.out.println(response.getFromUserEmail().get(0));
//        mockMvc.perform(get(url2)
//                        .with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
//                .andExpect(jsonPath("$.result.blackListId").value(1L))
//                .andExpect(jsonPath("$.result.startDate").exists())
//                .andExpect(jsonPath("$.result.fromUserEmail[0]").value(response.getFromUserEmail().get(0)))
//                .andDo(print());
//    }
//
//}