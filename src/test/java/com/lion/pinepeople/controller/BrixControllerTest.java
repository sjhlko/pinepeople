package com.lion.pinepeople.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lion.pinepeople.domain.dto.brix.BrixRequest;
import com.lion.pinepeople.service.BrixService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BrixController.class) //()에 작성된 클래스만 실제로 로드하여 테스트 진행
@MockBean(JpaMetamodelMappingContext.class)
class BrixControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean // 테스트할 클래스에서 주입받고 있는 객체에 대한 가짜 객체를 생성해주는 어노테이션
    BrixService brixService;

    @Autowired
    ObjectMapper objectMapper;

    String token;
    @Value("${jwt.token.secret}")
    private String key;

    @Test
    @DisplayName("별점으로 당도 평가")
    @WithMockUser
        //인증된 상태
    void brixCalculation_success() throws Exception {

        String url = "/api/users/1/brix";
        //데이터 만들기
        BrixRequest brixRequest = new BrixRequest(5);
        String result = "당도 측정 완료";
        //service 정의
        given(brixService.calculationBrix(any(), any(), any())).willReturn(result);

        //해당 url로 get요청
        mockMvc.perform(post(url)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(brixRequest)))
                .andExpect(status().isOk())
                //해당 내용이 있는지 테스트
                .andExpect(jsonPath("$.result").exists())
                .andExpect(jsonPath("$.result").value("당도 측정 완료"))
                .andDo(print());
    }

}