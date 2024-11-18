package com.common.point.service;

import com.common.point.dao.mapper.CompanyMapper;
import com.common.point.model.dto.Company;
import com.common.point.model.vo.CompanyVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyService {

	private final CompanyMapper companyMapper;

	@Transactional(readOnly = true)
	public List<Company> getCompanyListAll() throws Exception {

		List<Company> companyList = companyMapper.selectCompanyList();

		return companyList;
	}

	@Transactional(readOnly = false)
	public int postCreateCompany(Company company) throws Exception {

		CompanyVo companyVo = CompanyVo.builder()
				.companyName(company.getCompanyName())
				.companyRegNo(company.getCompanyRegNo())
				.build();


		int insertCompanyNo = companyMapper.insertCompany(companyVo);

		return insertCompanyNo;
	}

	@Transactional(readOnly = false)
	public ResponseEntity<String> postDeleteAllCompanies() throws Exception {

		companyMapper.deleteAllCompanies();

		return ResponseEntity.ok("All users deleted");
	}

}
