package com.common.point.controller;

import com.common.point.model.dto.Company;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PointReaderTest {

    @Autowired
    private ItemReader<List<Company>> pointReader;

    @Test
    public void testReader() throws Exception {
        // Reader 실행
        List<Company> company = pointReader.read();

        // Reader가 올바르게 데이터를 반환하는지 검증
        assertThat(company).isNotNull();
    }
}
