package com.common.point.controller;

import com.common.point.model.dto.Company;
import com.common.point.model.dto.Response;
import com.common.point.service.ExampleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@Controller
//@ResponseBody
//@PreAuthorize("")

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
@Slf4j
public class ExampleController {

	private final ExampleService exampleService;

	@GetMapping("/example-1")
	public ResponseEntity<Response<String>> getExample1() {
		Response<String> response = Response.<String>builder()
				.status(200)
				.message("Success")
				.data("Data")
				.build();
		return ResponseEntity.ok(response);
	}

	@GetMapping("/example-2")
	public ResponseEntity<Response<List<Company>>> getExample2() throws Exception {
		final List<Company> companyListAll = exampleService.getCompanyListAll();
		Response<List<Company>> response = Response.<List<Company>>builder()
				.status(200)
				.message("Success")
				.data(companyListAll)
				.build();

		return ResponseEntity.ok(response);
	}
}
