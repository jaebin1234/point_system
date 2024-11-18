package com.common.point.controller;

import com.common.point.model.dto.Company;
import com.common.point.model.vo.CompanyVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//@WebMvcTest(controllers = ExampleController.class)
@SpringBootTest
@AutoConfigureMockMvc
@Disabled
public class TestCompanyController {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;


	 private final List<String> companyNames = Arrays.asList(
            "삼성전자", "현대자동차", "SK하이닉스", "LG전자", "포스코",
            "카카오", "네이버", "셀트리온", "현대모비스", "삼성바이오로직스"
    );

//	@BeforeEach
//	public void startClearCompanyDatabase() throws Exception {
//		System.out.println("clearCompanyDatabase");
//		ResultActions perform = mockMvc.perform(post("/company/delete/all"));
//
//		perform
//				.andExpect(status().isOk())
//				.andDo(print());
//	}

	@Test
    public void createMultipleCompaniesTest() throws Exception {
        Random random = new Random();

        for (String companyName : companyNames) {
            String companyRegNo = String.format("%010d", random.nextInt(1_000_000_000));

            Company company = Company.builder()
                    .companyName(companyName)
                    .companyRegNo(companyRegNo)
                    .build();

            String requestBody = objectMapper.writeValueAsString(company);

            ResultActions perform = mockMvc.perform(post("/company/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
                    .accept(MediaType.APPLICATION_JSON)
            );

            perform.andExpect(status().isOk())
                    .andDo(print());
        }
    }

//	@Test
//	public void createCompanyTest() throws Exception {
//
//        Company company = Company.builder()
//				.companyName("테스트회사")
//				.companyRegNo("1234567890")
//				.build();
//
//		String requestBody = objectMapper.writeValueAsString(company);
//
//		ResultActions perform = mockMvc.perform(post("/company/create")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(requestBody)
//				.accept(MediaType.APPLICATION_JSON)
//		);
//
//		perform.andExpect(status().isOk())
//				.andDo(print());
//	}
}
