package com.common.point.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//@WebMvcTest(controllers = ExampleController.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ExampleControllerTest {

    @Autowired
    private MockMvc mockMvc;

//    @MockBean
//    private ExampleService exampleService; // 실제 빈 대신 Mock 사용

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    public void setUp() {
        // 초기화가 필요하면 이곳에 추가
    }

    @Test
    public void test() throws Exception {
        long startTime = System.currentTimeMillis();

        String company_no = "1";

        ResultActions perform = mockMvc.perform(get("/test/example-2")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .param("company_no", company_no)
        );
        perform
                .andExpect(status().isOk())
                .andDo(print());

        long endTime = System.currentTimeMillis();
        System.out.println("실행 시간: " + (endTime - startTime) + "ms");
    }
}
