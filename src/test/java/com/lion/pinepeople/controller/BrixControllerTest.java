//package com.lion.pinepeople.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.lion.pinepeople.domain.dto.brix.BrixRequest;
//import com.lion.pinepeople.domain.entity.Brix;
//import com.lion.pinepeople.domain.entity.User;
//import com.lion.pinepeople.exception.ErrorCode;
//import com.lion.pinepeople.exception.customException.AppException;
//import com.lion.pinepeople.service.BrixService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.mock;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(BrixController.class) //()에 작성된 클래스만 실제로 로드하여 테스트 진행
//@MockBean(JpaMetamodelMappingContext.class)
//class BrixControllerTest {
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @MockBean // 테스트할 클래스에서 주입받고 있는 객체에 대한 가짜 객체를 생성해주는 어노테이션
//    BrixService brixService;
//
//    @Autowired
//    ObjectMapper objectMapper;
//
//    String url = "/api/users/1/brix";
//
//    @Test
//    @DisplayName("별점으로 당도 평가")
//    @WithMockUser
//        //인증된 상태
//    void brixCalculation_success() throws Exception {
//
//        //데이터 만들기
//        BrixRequest brixRequest = new BrixRequest(5);
//        String result = "당도 측정 완료";
//        //service 정의
//        given(brixService.calculationBrix(any(), any(), any())).willReturn(result);
//
//        //해당 url로 post요청
//        mockMvc.perform(post(url)
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(brixRequest)))
//                .andExpect(status().isOk())
//                //해당 내용이 있는지 테스트
//                .andExpect(jsonPath("$.result").value("당도 측정 완료"))
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("당도 조회")
//    @WithMockUser
//    void getBrix_success() throws Exception {
//
//        //데이터 만들기
//        User user = mock(User.class);
//
//        Brix brix = Brix.builder()
//                .brixFigure(1.4)
//                .brixName("brixName")
//                .user(user)
//                .build();
//
//        //service 정의
//        given(brixService.getBrix(any(), any())).willReturn(brix.getBrixFigure());
//
//        //해당 url로 get요청
//        mockMvc.perform(get(url)
//                        .with(csrf()))
//                .andExpect(status().isOk())
//                //해당 내용이 있는지 테스트
//                .andExpect(jsonPath("$.result").value(brix.getBrixFigure()))
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("로그인 실패 시")
//    @WithMockUser
//    void getBrix_fail_not_login() throws Exception {
//
//        //service 정의
//        given(brixService.getBrix(any(), any())).willThrow(new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));
//
//        //해당 url로 get요청
//        mockMvc.perform(get(url)
//                        .with(csrf()))
//                .andExpect(status().isNotFound())
//                //해당 내용이 있는지 테스트
//                .andExpect(jsonPath("$.result.errorCode").value("USER_NOT_FOUND"))
//                .andExpect(jsonPath("$.result.message").value(ErrorCode.USER_NOT_FOUND.getMessage()))
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("해당 당도가 조회가 되지 않을 때")
//    @WithMockUser
//    void getBrix_fail_non_brix() throws Exception {
//
//        //service 정의
//        given(brixService.getBrix(any(), any())).willThrow(new AppException(ErrorCode.BRIX_NOT_FOUND, ErrorCode.BRIX_NOT_FOUND.getMessage()));
//
//        //해당 url로 get요청
//        mockMvc.perform(get(url)
//                        .with(csrf()))
//                .andExpect(status().isConflict())
//                //해당 내용이 있는지 테스트
//                .andExpect(jsonPath("$.result.errorCode").value("BRIX_NOT_FOUND"))
//                .andExpect(jsonPath("$.result.message").value(ErrorCode.BRIX_NOT_FOUND.getMessage()))
//                .andDo(print());
//    }
//
//}