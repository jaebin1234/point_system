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

import java.util.concurrent.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PointUseControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void testConcurrentPointUse() throws Exception {
        System.out.println("start testConcurrentPointUse()");

		// 테스트에 사용할 회사 번호
		int companyNo = 35;


		// ExecutorService: 병렬 작업을 관리하는 Java의 스레드 풀 제공 클래스
		// FixedThreadPool(2): 두 개의 고정된 스레드로 구성된 스레드 풀 생성
		ExecutorService executorService = Executors.newFixedThreadPool(2);

		// 첫 번째 사용자 (userNo: 33)의 포인트 차감 작업
		Runnable task1 = () -> {
			try {
				// PointChargeAndUseRequest: 포인트 차감 요청을 나타내는 DTO
				PointChargeAndUseRequest request = PointChargeAndUseRequest.builder()
						.companyNo(companyNo) // 회사 번호
						.userNo(33)          // 사용자 번호 (31번 사용자)
						.point(100)           // 차감할 포인트 (10포인트)
						.pointActionType("U") // 액션 유형 (U: 차감)
						.description("사용자 33 동시 100 포인트 차감 테스트") // 요청 설명
						.build();

				// ObjectMapper: Java 객체를 JSON 문자열로 변환하는 유틸리티 클래스
				String requestBody = objectMapper.writeValueAsString(request);

				// MockMvc: 컨트롤러 테스트를 지원하는 Spring의 테스트 유틸리티
				// post("/point/use"): "/point/use" 경로에 POST 요청 전송
				mockMvc.perform(post("/point/use")
								.contentType(MediaType.APPLICATION_JSON) // 요청 본문의 MIME 타입 지정
								.content(requestBody) // JSON으로 변환된 요청 본문 추가
								.accept(MediaType.APPLICATION_JSON)) // 서버 응답의 MIME 타입 기대값 지정
						.andExpect(status().isOk()) // HTTP 200 OK 응답 기대
						.andDo(print()); // 응답과 요청 정보를 출력
			} catch (Exception e) {
				e.printStackTrace(); // 예외 발생 시 스택 트레이스 출력
			}
		};

		// 두 번째 사용자 (userNo: 34)의 포인트 차감 작업
		Runnable task2 = () -> {
			try {
				PointChargeAndUseRequest request = PointChargeAndUseRequest.builder()
						.companyNo(companyNo) // 회사 번호
						.userNo(34)          // 사용자 번호 (32번 사용자)
						.point(200)           // 차감할 포인트 (200포인트)
						.pointActionType("U") // 액션 유형 (U: 차감)
						.description("사용자 34 동시 200 포인트 차감 테스트") // 요청 설명
						.build();

				String requestBody = objectMapper.writeValueAsString(request);

				mockMvc.perform(post("/point/use")
								.contentType(MediaType.APPLICATION_JSON)
								.content(requestBody)
								.accept(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk())
						.andDo(print());
			} catch (Exception e) {
				e.printStackTrace();
			}
		};

		// submit(): ExecutorService에 작업을 제출하여 병렬로 실행
        final Future<?> submit = executorService.submit(task1);// 첫 번째 사용자 작업 실행
        final Future<?> submit1 = executorService.submit(task2);// 두 번째 사용자 작업 실행

        // ExecutorService 종료 후 모든 작업 완료 대기
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                System.out.println("작업이 완료되지 않았습니다.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
}
        System.out.println("end testConcurrentPointUse()");
	}


	@Test
	public void testConcurrentPointUse2() throws Exception {
		int companyNo = 35;
		int point = 50;

		// CompletableFuture: 비동기 작업을 처리하는 클래스
		// 두 개의 병렬 작업을 실행하는 CompletableFuture 생성
		CompletableFuture<Void> task1 = CompletableFuture.runAsync(() -> {
			try {
				PointChargeAndUseRequest request = PointChargeAndUseRequest.builder()
						.companyNo(companyNo)
						.userNo(33) // 사용자 33
						.point(point)
						.pointType("E")
						.pointActionType("U")
						.description("사용자 33 동시 포인트 차감 테스트")
						.build();

				String requestBody = objectMapper.writeValueAsString(request);

				mockMvc.perform(post("/point/use")
								.contentType(MediaType.APPLICATION_JSON)
								.content(requestBody)
								.accept(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk())
						.andDo(print());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		CompletableFuture<Void> task2 = CompletableFuture.runAsync(() -> {
			try {
				PointChargeAndUseRequest request = PointChargeAndUseRequest.builder()
						.companyNo(companyNo)
						.userNo(34) // 사용자 34
						.point(point)
						.pointType("E")
						.pointActionType("U")
						.description("사용자 34 동시 포인트 차감 테스트")
						.build();

				String requestBody = objectMapper.writeValueAsString(request);

				mockMvc.perform(post("/point/use")
								.contentType(MediaType.APPLICATION_JSON)
								.content(requestBody)
								.accept(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk())
						.andDo(print());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		// 두 작업이 모두 완료될 때까지 기다린다
		CompletableFuture.allOf(task1, task2).join();
	}


	@Test
	public void testPaidPointUse() throws Exception {
		PointChargeAndUseRequest request = PointChargeAndUseRequest.builder()
//                .companyNo(35)
				.companyNo(36)
				.userNo(33)
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
