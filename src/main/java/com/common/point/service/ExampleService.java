package com.common.point.service;

import com.common.point.dao.mapper.CompanyMapper;
import com.common.point.model.dto.Company;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExampleService {

	private final CompanyMapper companyMapper;

	@Transactional(readOnly = true)
	public List<Company> getCompanyListAll() throws Exception {
//		log.debug("This is a DEBUG log message.");
//		log.info("This is an INFO log message.");
//		log.warn("This is a WARN log message.");
//		log.error("This is an ERROR log message.");

		// error 코드 삽입
//		int arr[] = new int[2];
//		arr[2] = 0;

		List<Company> companyList = companyMapper.selectCompanyList();

		return companyList;
	}

}
