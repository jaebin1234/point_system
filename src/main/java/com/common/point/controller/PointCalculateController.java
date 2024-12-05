package com.common.point.controller;

import com.common.point.model.dto.PointCalculateRequest;
import com.common.point.model.dto.PointCalculateResponse;
import com.common.point.model.dto.Response;
import com.common.point.service.PointCalcService;
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
public class PointCalculateController {

	private final PointCalcService pointCalcService;

	@PostMapping("/calculate")
	public ResponseEntity<Response<PointCalculateResponse>> postPointCalc(@RequestBody PointCalculateRequest pointCalculateRequest) throws Exception {
		final PointCalculateResponse pointCalculateResponse = pointCalcService.postPointCalc(pointCalculateRequest);

		Response<PointCalculateResponse> response = Response.<PointCalculateResponse>builder()
				.status(200)
				.message("Success")
				.data(pointCalculateResponse)
				.build();

		return ResponseEntity.ok(response);
	}

}
