package com.common.point.service;

import com.common.point.dao.mapper.CompanyMapper;
import com.common.point.model.dto.Company;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Slf4j
class TestExampleService {

	private ExampleService exampleService;
	private CompanyMapper companyMapper;

	@BeforeEach
	void setUp() {
		// Mock 객체 생성
		companyMapper = Mockito.mock(CompanyMapper.class);
		exampleService = new ExampleService(companyMapper);
	}

	@Test
	@DisplayName("getCompanyList() 테스트")
	void testGetCompanyList() throws Exception {
		System.out.println("getCompanyList() 테스트");

		System.out.println("JASYPT_ENCRYPTOR_PASSWORD: " + System.getProperty("JASYPT_ENCRYPTOR_PASSWORD"));


		Company company1 = new Company(1, "테스트 회사1", "1111111111");

		List<Company> mockCompanyList = Arrays.asList(company1);
		when(companyMapper.selectCompanyList()).thenReturn(mockCompanyList);

		// 테스트 실행
		List<Company> result = exampleService.getCompanyListAll();

		// 결과 검증
		assertEquals(1, result.size());
		assertEquals(company1.getCompanyNo(), result.get(0).getCompanyNo());

		assertAll(
				() -> assertEquals(1, result.size()),
				() -> System.out.println("All assertions passed!")
		);

		assertAll(
				() -> assertEquals(company1.getCompanyNo(), result.get(0).getCompanyNo()),
				() -> System.out.println("All assertions passed!")
		);

		// 메서드 호출 여부 검증
		verify(companyMapper, times(1)).selectCompanyList();
	}

}
