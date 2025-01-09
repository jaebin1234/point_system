package com.common.point.service.batch;

import com.common.point.dao.mapper.PointCalculateMapper;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
@StepScope
public class PointWriter implements ItemWriter<List<HashMap<String, Object>>> {

	private final PointCalculateMapper pointCalculateMapper;

	public PointWriter(PointCalculateMapper pointCalculateMapper) {
		this.pointCalculateMapper = pointCalculateMapper;
	}

	@Override
	public void write(Chunk<? extends List<HashMap<String, Object>>> chunk) throws Exception {
		for (List<HashMap<String, Object>> dataList : chunk) {
			for (HashMap<String, Object> data : dataList) {
				try {
					int affectedRows = pointCalculateMapper.upsertPointYearCalc(data);
					System.out.println("Writer: 업서트 성공 - 영향받은 행: " + affectedRows);
				} catch (Exception e) {
					System.err.println("Writer: 업서트 실패: " + data.get("companyNo"));
					e.printStackTrace();
				}
			}
		}
	}
}
