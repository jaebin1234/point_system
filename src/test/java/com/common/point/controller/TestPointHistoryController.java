package com.common.point.controller;

import com.common.point.model.dto.PointChargeAndUseRequest;
import com.common.point.model.dto.PointHistoryRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TestPointHistoryController {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;


	@Test
	public void testPointHistoryExists() throws Exception {
		String dateTimeString = "2024-11-27 18:17:39.929273";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");


		PointHistoryRequest request = PointHistoryRequest.builder()
                .companyNo(35)
				.userNo(34)
				.point(100)
				.pointActionType("U") // 차감
				.pointGroupKey("7d2e8e4908fb4996aa08")
//				.pointGroupKey("7d2e8e4908fb4996aa09")
				.insertTimestamp(LocalDateTime.parse(dateTimeString,formatter))
				.build();

		String requestBody = objectMapper.writeValueAsString(request);

		ResultActions result = mockMvc.perform(post("/point/history/exists")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody)
				.accept(MediaType.APPLICATION_JSON)
		);

		result.andExpect(status().isOk())


				.andDo(print());
	}
}
