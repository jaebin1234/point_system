package com.common.point.controller;

import com.common.point.model.dto.Company;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PointProcessorTest {

    @Autowired
    private ItemProcessor<List<Company>, List<HashMap<String, Object>>> pointProcessor;

    @Test
    public void testProcessor() throws Exception {
        // Given
        List<Company> companies = new ArrayList<>();

        Company company = new Company();
        company.setCompanyNo(35); // 예제 데이터

        companies.add(company);

        // When
        List<HashMap<String, Object>> processedDataList = pointProcessor.process(companies);

        // Then
        assertThat(processedDataList).isNotNull();
    }
}
