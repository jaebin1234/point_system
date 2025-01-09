package com.common.point.service.batch;

import com.common.point.dao.mapper.PointCalculateMapper;
import com.common.point.model.dto.Company;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.common.point.model.dto.PointKeys.*;


@Component
@StepScope
public class PointProcessor implements ItemProcessor<List<Company>, List<HashMap<String, Object>>> {

	private final PointCalculateMapper pointCalculateMapper;

	public PointProcessor(PointCalculateMapper pointCalculateMapper) {
		this.pointCalculateMapper = pointCalculateMapper;
	}

	private HashMap<String, Object> processCompany(Company company) throws Exception {
		// 정산 파라미터 설정
		HashMap<String, Object> params = new HashMap<>();
		// 현재 날짜 기반 yyyymm 값 설정
//		String yyyymm = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMM"));
//		params.put("yyyymm", yyyymm);

		String yyyymm = "202412";
		String yyyy = yyyymm.substring(0, 4);
		String mm = yyyymm.substring(4, 6);
		LocalDateTime dateTime = LocalDateTime.of(Integer.parseInt(yyyy), Integer.parseInt(mm), 1, 0, 0);

		params.put("yyyymm", yyyymm); // 예시 값
		params.put("dateTime", dateTime);
		params.put("companyNo", company.getCompanyNo());

		// 잔여 포인트 및 액션 포인트 조회
		Map<String, Object> remainPointInfo = pointCalculateMapper.selectRemainingPoint(params);
		Map<String, Object> actionPointInfo = pointCalculateMapper.selectActionPoint(params);

		if (remainPointInfo == null || actionPointInfo == null) {
			throw new IllegalStateException("Processor: 포인트 정보 조회 실패");
		}

		int cmm = Integer.parseInt(mm);

		// 결과 데이터 조합
		HashMap<String, Object> result = new HashMap<>();
		result.put("companyNo", company.getCompanyNo());
		result.put("yyyymm", yyyymm);
		result.put("yyyy", yyyy);

		// 기본 잔여 포인트 추가
		result.put(BASIC_PAID_POINT_ + mm, remainPointInfo.getOrDefault(BASIC_PAID_POINT, 0));
		result.put(BASIC_FREE_POINT_ + mm, remainPointInfo.getOrDefault(BASIC_FREE_POINT, 0));
		result.put(END_PAID_POINT_ + mm, remainPointInfo.getOrDefault(END_PAID_POINT, 0));
		result.put(END_FREE_POINT_ + mm, remainPointInfo.getOrDefault(END_FREE_POINT, 0));

		for (int i = cmm + 1; i <= 12; i++) {
			String nextMonth = i + "";
			if (i < 10) {
				nextMonth = "0" + i;
			}
			result.put(BASIC_PAID_POINT_ + nextMonth, remainPointInfo.getOrDefault(BASIC_PAID_POINT, 0)); // 충전
			result.put(BASIC_FREE_POINT_ + nextMonth, remainPointInfo.getOrDefault(BASIC_FREE_POINT, 0)); // 적립
			result.put(END_PAID_POINT_ + nextMonth, remainPointInfo.getOrDefault(END_PAID_POINT, 0));
			result.put(END_FREE_POINT_ + nextMonth, remainPointInfo.getOrDefault(END_FREE_POINT, 0));
		}

		// 액션 포인트 추가
		result.put(CHARGE_PAID_POINT_ + mm, actionPointInfo.getOrDefault(CHARGE_PAID_POINT, 0));
		result.put(CHARGE_FREE_POINT_ + mm, actionPointInfo.getOrDefault(CHARGE_FREE_POINT, 0));
		result.put(USE_PAID_POINT_ + mm, actionPointInfo.getOrDefault(USE_PAID_POINT, 0));
		result.put(USE_FREE_POINT_ + mm, actionPointInfo.getOrDefault(USE_FREE_POINT, 0));


		return result;
	}

	@Override
	public List<HashMap<String, Object>> process(List<Company> companies) throws Exception {
		List<HashMap<String, Object>> results = new ArrayList<>();

		// 각 회사 데이터를 병렬 처리
		for (Company company : companies) {
			HashMap<String, Object> map = processCompany(company);
			results.add(map);
		}

		return results; // 결과 리스트 반환

	}
}
