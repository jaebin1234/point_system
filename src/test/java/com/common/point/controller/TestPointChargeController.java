package com.common.point.controller;

import com.common.point.dao.mapper.CompanyMapper;
import com.common.point.dao.mapper.UserMapper;
import com.common.point.model.dto.Company;
import com.common.point.model.dto.PUser;
import com.common.point.model.dto.PointChargeAndUseRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Random;

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

	@Autowired
	private CompanyMapper companyMapper;

	@Autowired
	private UserMapper userMapper;

	@Test
    public void testRandomPointCharge() throws Exception {
		final Random random = new Random();

		final List<Company> companyList = companyMapper.selectCompanyList();
		final List<PUser> pUserList = userMapper.selectPUserList();

        for (int i = 0; i < companyList.size(); i++) {
            for (int j = 0; j < pUserList.size(); j++) {
                boolean isPaid = random.nextBoolean(); // 유상/무상 포인트 결정
                int randomPoint = (random.nextInt(30) + 1) * 100;

                PointChargeAndUseRequest pointChargeAndUseRequest = PointChargeAndUseRequest.builder()
                        .companyNo(companyList.get(i).getCompanyNo())
                        .userNo(pUserList.get(j).getUserNo())
                        .point(randomPoint)
                        .pointType(isPaid ? "C" : "E") // C: 유상, E: 무상
                        .pointActionType("A") // A: 충전
                        .description(isPaid ? "유상 포인트 충전" : "무상 포인트 충전")
                        .build();

                String requestBody = objectMapper.writeValueAsString(pointChargeAndUseRequest);

                ResultActions perform = mockMvc.perform(post("/point/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON)
                );

                perform.andExpect(status().isOk())
                        .andDo(print());
            }
        }
    }


	@Test
	public void testPaidPointCharge() throws Exception {

		PointChargeAndUseRequest pointChargeAndUseRequest = PointChargeAndUseRequest.builder()
				.companyNo(35)
//				.companyNo(37)
//				.companyNo(38)
//				.userNo(33)
				.userNo(34)
				.point(1000)
				.pointType("C")
//				.pointActionType("D")
				.pointActionType("A")
				.description("유상 포인트 충전 테스트")
				.build();

		String requestBody = objectMapper.writeValueAsString(pointChargeAndUseRequest);


		ResultActions perform = mockMvc.perform(post("/point/charge")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody)
				.accept(MediaType.APPLICATION_JSON)
		);

		perform.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	public void testFreePointCharge() throws Exception {

		PointChargeAndUseRequest pointChargeAndUseRequest = PointChargeAndUseRequest.builder()
//				.companyNo(35)
				.companyNo(36)
				.userNo(33)
				.point(200)
				.pointType("E")
				.pointActionType("A")
				.description("무상 포인트 충전 테스트")
				.build();

		String requestBody = objectMapper.writeValueAsString(pointChargeAndUseRequest);


		ResultActions perform = mockMvc.perform(post("/point/charge")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody)
				.accept(MediaType.APPLICATION_JSON)
		);

		perform.andExpect(status().isOk())
				.andDo(print());
	}
}
