package com.common.point.controller;

import com.common.point.model.dto.Company;
import com.common.point.model.dto.PointChargeRequest;
import com.common.point.model.dto.PointChargeResponse;
import com.common.point.service.PointChargeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//@WebMvcTest(PointChargeController.class)
@SpringBootTest
@AutoConfigureMockMvc
@Disabled
public class TestPointChargeController {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

//	@MockBean
//	private PointChargeService pointChargeService;

	@Test
	public void testPaidPointCharge() throws Exception {

		PointChargeRequest pointChargeRequest = PointChargeRequest.builder()
				.companyNo(25)
				.userNo(32)
				.point(1000)
				.pointType("C")
//				.pointActionType("D")
				.pointActionType("A")
				.description("유상 포인트 충전 테스트")
				.build();

		String requestBody = objectMapper.writeValueAsString(pointChargeRequest);


		ResultActions perform = mockMvc.perform(post("/point/charge/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody)
				.accept(MediaType.APPLICATION_JSON)
		);

		perform.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	public void testFreePointCharge() throws Exception {

		PointChargeRequest pointChargeRequest = PointChargeRequest.builder()
				.companyNo(25)
				.userNo(32)
				.point(250)
				.pointType("E")
				.pointActionType("A")
				.description("무상 포인트 충전 테스트")
				.build();

		String requestBody = objectMapper.writeValueAsString(pointChargeRequest);


		ResultActions perform = mockMvc.perform(post("/point/charge/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody)
				.accept(MediaType.APPLICATION_JSON)
		);

		perform.andExpect(status().isOk())
				.andDo(print());
	}
}
