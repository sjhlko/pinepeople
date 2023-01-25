package com.lion.pinepeople.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lion.pinepeople.service.ReportService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReportController.class)
@MockBean(JpaMetamodelMappingContext.class)
class ReportControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    ReportService reportService;

    @Autowired
    ObjectMapper objectMapper;

    String url = "/api/users/1/reports";
    @Test
    @DisplayName("신고 성공")
    @WithMockUser
    void report_success() throws Exception{
        String response = "신고가 정상적으로 접수되었습니다.";
        given(reportService.addReport(any(), any())).willReturn(response);

        mockMvc.perform(post(url)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result").value(response))
                .andDo(print());
    }

}