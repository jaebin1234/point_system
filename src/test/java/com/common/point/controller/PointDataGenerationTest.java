package com.common.point.controller;

import com.common.point.dao.mapper.CompanyMapper;
import com.common.point.dao.mapper.UserMapper;
import com.common.point.model.dto.Company;
import com.common.point.model.dto.PUser;
import com.common.point.model.dto.PointChargeAndUseRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Commit
public class PointDataGenerationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private CompanyMapper companyMapper;

	@Autowired
	private UserMapper userMapper;

	@Test
	public void generateChargeAndUseData() throws Exception {
		List<Company> companies = companyMapper.selectCompanyList();
		List<PUser> users = userMapper.selectPUserList();
		Random random = new Random();

		for (Company company : companies) {
			for (PUser user : users) {

				// 첫 번째는 항상 충전으로 시작
				PointChargeAndUseRequest initialChargeRequest = PointChargeAndUseRequest.builder()
						.companyNo(company.getCompanyNo())
						.userNo(user.getUserNo())
						.point((random.nextInt(50) + 1) * 1000) // 충전: 1000 ~ 50000 포인트
						.pointType(random.nextBoolean() ? "C" : "E") // 유상(C) 또는 무상(E) 포인트
						.pointActionType("A") // 충전
						.description("초기 포인트 충전")
						.build();

				mockMvc.perform(post("/point/charge")
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsString(initialChargeRequest)))
						.andExpect(status().isOk())
						.andDo(print());

				for (int i = 0; i < 10; i++) { // 각 사용자/회사당 10개의 기록 생성
					boolean isCharge = random.nextBoolean(); // 충전(A) 또는 차감(U) 랜덤 선택
					int pointAmount = isCharge
							? (random.nextInt(50) + 1) * 100 // 충전: 100 ~ 5000 포인트
							: (random.nextInt(5) + 1) * 100; // 차감: 100 ~ 500 포인트

					PointChargeAndUseRequest request = PointChargeAndUseRequest.builder()
							.companyNo(company.getCompanyNo())
							.userNo(user.getUserNo())
							.point(pointAmount)
							.pointType(random.nextBoolean() ? "C" : "E") // 유상(C) 또는 무상(E) 포인트
							.pointActionType(isCharge ? "A" : "U")
							.description(isCharge ? "포인트 충전" : "포인트 차감")
							.build();

					// 충전 또는 차감 요청
					String url = isCharge ? "/point/charge" : "/point/use";

					ResultActions perform = mockMvc.perform(post(url)
									.contentType(MediaType.APPLICATION_JSON)
									.content(objectMapper.writeValueAsString(request)))
							.andExpect(status().isOk());

					perform.andExpect(status().isOk())
							.andDo(print());
				}
			}
		}
	}
}
