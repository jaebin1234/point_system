package com.common.point.controller;

import com.common.point.model.dto.PointChargeAndUseRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TestPointUseController {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;



    @Test
    public void testPaidPointUse() throws Exception {
        PointChargeAndUseRequest request = PointChargeAndUseRequest.builder()
//                .companyNo(25)
                .companyNo(26)
                .userNo(31)
                .point(100)
//                .pointActionType("A")
                .pointActionType("U") // 차감
                .description("포인트 차감 테스트")
                .build();

        String requestBody = objectMapper.writeValueAsString(request);

        ResultActions result = mockMvc.perform(post("/point/use")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andDo(print());
    }
}
