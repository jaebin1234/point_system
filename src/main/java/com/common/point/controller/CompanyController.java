package com.common.point.controller;

import com.common.point.model.dto.Company;
import com.common.point.model.dto.Response;
import com.common.point.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
@Slf4j
public class CompanyController {

	final CompanyService companyService;

	@GetMapping("/list/all")
	public ResponseEntity<Response<List<Company>>> getCompanyListAll() throws Exception {
		final List<Company> companyListAll = companyService.getCompanyListAll();
		Response<List<Company>> response = Response.<List<Company>>builder()
				.status(200)
				.message("Success")
				.data(companyListAll)
				.build();

		return ResponseEntity.ok(response);
	}

	@PostMapping("/create")
	public ResponseEntity<Response<Integer>> postCreateCompany(@RequestBody Company company) throws Exception {
		final int createCompanyNo = companyService.postCreateCompany(company);
		Response<Integer> response = Response.<Integer>builder()
				.status(200)
				.message("Success")
				.data(createCompanyNo)
				.build();

		return ResponseEntity.ok(response);
	}

	@PostMapping("/delete/all")
    public ResponseEntity<String> postDeleteAllCompanies() throws Exception{

		companyService.postDeleteAllCompanies();

        return ResponseEntity.ok("All companies deleted");
    }



}
