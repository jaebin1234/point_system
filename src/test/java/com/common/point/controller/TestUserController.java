package com.common.point.controller;

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

import java.util.Random;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//@WebMvcTest(controllers = UserController.class)
@SpringBootTest
@AutoConfigureMockMvc
@Disabled
public class TestUserController {

	@Autowired
	private MockMvc mockMvc;

//    @MockBean
//    private UserService userService; // 실제 빈 대신 Mock 사용

	@Autowired
	private ObjectMapper mapper;

	@BeforeEach
	public void setUp() {
		// 초기화가 필요하면 이곳에 추가
	}

	private static final String[] LAST_NAMES = {"김", "이", "박", "최", "정", "권", "조", "윤", "장", "임"};
	private static final String[] FIRST_NAMES = {"민", "서", "현", "지", "은", "하", "진", "유", "성", "빈", "재"};

	private String generateRandomKoreanName() {
		Random random = new Random();
		String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
		String firstName1 = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
		String firstName2 = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
		return lastName + firstName1 + firstName2;
	}

	@BeforeEach
	public void clearUserDatabase() throws Exception {
		System.out.println("clearUserDatabase");
		ResultActions perform = mockMvc.perform(get("/user/deleteAll"));

		perform
				.andExpect(status().isOk())
				.andDo(print());

	}

	@Test
	public void createMultipleRandomKoreanUsers() throws Exception {
		System.out.println("createMultipleRandomKoreanUsers");
		IntStream.range(0, 30).forEach(i -> {
			try {
				String randomName = generateRandomKoreanName();
				ResultActions perform = mockMvc.perform(post("/user/create")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.accept(MediaType.APPLICATION_JSON)
						.param("userName", randomName)
				);
				perform
						.andExpect(status().isOk())
						.andDo(print());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
