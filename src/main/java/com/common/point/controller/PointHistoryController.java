package com.common.point.controller;

import com.common.point.model.dto.PointChargeAndUseResponse;
import com.common.point.model.dto.PointHistory;
import com.common.point.model.dto.PointHistoryRequest;
import com.common.point.model.dto.Response;
import com.common.point.service.PointHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/point")
@RequiredArgsConstructor
@Slf4j
public class PointHistoryController {

	private final PointHistoryService pointHistoryService;

	@PostMapping("/history/list")
	public ResponseEntity<Response<List<PointHistory>>> getPointHistoryList(@RequestBody PointHistoryRequest pointHistoryRequest) throws Exception {

		final List<PointHistory> pointHistoryList = pointHistoryService.getPointHistoryList(pointHistoryRequest);

		Response<List<PointHistory>> response = Response.<List<PointHistory>>builder()
				.status(200)
				.message("Success")
				.data(pointHistoryList)
				.build();

		return ResponseEntity.ok(response);
	}

	@PostMapping("/history/exists")
	public ResponseEntity<Response<Boolean>> getPointHistoryListExists(@RequestBody PointHistoryRequest pointHistoryRequest) throws Exception {

		final boolean isExists = pointHistoryService.getPointHistoryListExists(pointHistoryRequest);

		Response<Boolean> response = Response.<Boolean>builder()
				.status(200)
				.message("Success")
				.data(isExists)
				.build();

		return ResponseEntity.ok(response);
	}

}
