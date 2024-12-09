package com.common.point.controller;

import com.common.point.model.dto.PointChargeAndUseRequest;
import com.common.point.model.dto.PointChargeAndUseResponse;
import com.common.point.model.dto.Response;
import com.common.point.service.PointUseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/point")
@RequiredArgsConstructor
@Slf4j
public class PointUseController {

	private final PointUseService pointUseService;

	//현재 auth 시스템이 존재 하지않아 company_no와 user_no를 직접 받습니다.
	//원래는 Oauth 2.0 방식으로 토큰서버를 따로 두고, 해당 토큰으로 인증 과정을 거친 뒤, 세션 데이터에서 company_no와 user_no를 가져옵니다.
	@PostMapping("/use")
	public ResponseEntity<Response<PointChargeAndUseResponse>> postPointUse(@RequestBody PointChargeAndUseRequest pointChargeAndUseRequest) throws Exception {

		final PointChargeAndUseResponse pointChargeAndUseResponse = pointUseService.postPointUseIF(pointChargeAndUseRequest);

		Response<PointChargeAndUseResponse> response = Response.<PointChargeAndUseResponse>builder()
				.status(200)
				.message("Success")
				.data(pointChargeAndUseResponse)
				.build();

		return ResponseEntity.ok(response);
	}

}
