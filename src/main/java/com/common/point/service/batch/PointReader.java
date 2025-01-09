package com.common.point.service.batch;

import com.common.point.dao.mapper.PointCalculateMapper;
import com.common.point.model.dto.Company;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Component
@StepScope
public class PointReader implements ItemReader<List<Company>> {

	private final PointCalculateMapper pointCalculateMapper;

	private boolean hasRun = false; // 실행 여부를 저장하는 플래그

	@Value("#{stepExecutionContext['limitSize']}")
	private long limitSize;

	@Value("#{stepExecutionContext['totalThreadCount']}")
	private long totalThreadCount;

	@Value("#{stepExecutionContext['threadIndex']}")
	private int threadIndex;

	private int pageNumber = 0;

	public PointReader(PointCalculateMapper pointCalculateMapper) {
		this.pointCalculateMapper = pointCalculateMapper;
	}

	@Override
	public List<Company> read() throws Exception {
		if (hasRun) {
			// 한 번 실행한 이후에는 null 반환
			return null;
		}

		// 현재 스레드에 할당된 offset 계산
		long offset = (threadIndex + pageNumber * totalThreadCount) * limitSize;

		System.out.println("ThreadIndex: " + threadIndex + ", Offset: " + offset + ", limit: " + limitSize);

//		String lastSyncYmdhms = null;
//		String lastSyncYmdhms_end = null;

//            // region lastSyncYmdhms 마지막 동기화 시간 조회
//            final HashMap hashMap = pointCalculateMapper.selectPointYearLastSyncTime();
//            lastSyncYmdhms = hashMap.get("lastSyncYmdhms").toString();
//            lastSyncYmdhms_end = hashMap.get("lastSyncYmdhms_end").toString();
//            // endregion

		// region yyyymm 기본 현재 연월
//            String yyyymm = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
		String yyyymm = "202412"; // 예시 값
		String yyyy = yyyymm.substring(0, 4);
		String mm = yyyymm.substring(4, 6);
		LocalDateTime dateTime = LocalDateTime.of(Integer.parseInt(yyyy), Integer.parseInt(mm), 1, 0, 0);
		// endregion

		// 파라미터를 설정하여 회사 정보를 조회
		HashMap<String, Object> targetParam = new HashMap<>();
//		targetParam.put("lastSyncYmdhms", lastSyncYmdhms);
//		targetParam.put("lastSyncYmdhms_end", lastSyncYmdhms_end);
		targetParam.put("yyyymm", yyyymm);
		targetParam.put("dateTime", dateTime);
		targetParam.put("limit", limitSize);
		targetParam.put("offset", offset);

		List<Company> companies = pointCalculateMapper.selectPointCalcTargetList(targetParam);

		if (companies == null || companies.isEmpty()) {
			return null;
		}

		pageNumber++;

		hasRun = true;
		return companies;
	}
}
