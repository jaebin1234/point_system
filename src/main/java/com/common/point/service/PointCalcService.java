package com.common.point.service;

import com.common.point.dao.mapper.PointCalculateMapper;
import com.common.point.model.dto.Company;
import com.common.point.model.dto.PointCalculateRequest;
import com.common.point.model.dto.PointCalculateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.common.point.model.dto.PointKeys.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PointCalcService {

	private final PointCalculateMapper pointCalculateMapper;

	public PointCalculateResponse postPointCalc(PointCalculateRequest pointCalculateRequest) throws Exception{
		PointCalculateResponse pointCalculateResponse = new PointCalculateResponse();
		List<Company> successCompanyList = new ArrayList<>();
		List<Company> failCompanyList = new ArrayList<>();
		final boolean realTimeCalculation = pointCalculateRequest.isRealTimeCalculation();

		// region lastSyncYmdhms 마지막 동기화 시간 조회
		String lastSyncYmdhms = null;
		String lastSyncYmdhms_end = null;
		final HashMap hashMap = pointCalculateMapper.selectPointYearLastSyncTime();
		if(realTimeCalculation){
			lastSyncYmdhms = hashMap.get("lastSyncYmdhms").toString();
			lastSyncYmdhms_end = hashMap.get("lastSyncYmdhms_end").toString();
		}
		// endregion

		// region yyyymm 기본 현재 연월
		String yyyymm = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
		if(pointCalculateRequest.getYyyymm() != null){
			yyyymm = pointCalculateRequest.getYyyymm();
		}
		String yyyy = yyyymm.substring(0,4);
		String mm = yyyymm.substring(4,6);
		LocalDateTime dateTime = LocalDateTime.of(Integer.parseInt(yyyy), Integer.parseInt(mm), 1, 0, 0);
		// endregion

		// region 정산 타깃 회사 조회
		HashMap targetParam = new HashMap();
		targetParam.put("lastSyncYmdhms", lastSyncYmdhms);
		targetParam.put("lastSyncYmdhms_end", lastSyncYmdhms_end);
		targetParam.put("companyRegNo", pointCalculateRequest.getCompanyRegNo());
		targetParam.put("companyNo", pointCalculateRequest.getCompanyNo());
		targetParam.put("yyyymm", yyyymm);
		targetParam.put("dateTime", dateTime);
		List<Company> pointCalcTargetList = pointCalculateMapper.selectPointCalcTargetList(targetParam);
		// endregion

		// region 포인트 정산
		if (pointCalcTargetList != null) {
			for(Company pointCalcTarget : pointCalcTargetList){
				Integer companyNo = pointCalcTarget.getCompanyNo();
				HashMap<String, Object> upsertParams = new HashMap<>();

				// region 기초, 잔여 포인트 조회
				HashMap remainParam = new HashMap();
				remainParam.put("companyNo", companyNo);
				remainParam.put("yyyymm", yyyymm);
				remainParam.put("dateTime", dateTime);
				HashMap remainPointInfo = pointCalculateMapper.selectRemainingPoint(remainParam);

				if (remainPointInfo == null) {
					remainPointInfo = new HashMap();
				}

				upsertParams.put(BASIC_PAID_POINT_ + mm, remainPointInfo.getOrDefault(BASIC_PAID_POINT,0));
				upsertParams.put(BASIC_FREE_POINT_ + mm, remainPointInfo.getOrDefault(BASIC_FREE_POINT,0));
				upsertParams.put(END_PAID_POINT_ + mm, remainPointInfo.getOrDefault(END_PAID_POINT,0));
				upsertParams.put(END_FREE_POINT_ + mm, remainPointInfo.getOrDefault(END_FREE_POINT,0));

				int currentMonth = Integer.parseInt(mm);

				for (int i = currentMonth + 1; i <= 12; i++) {
					String nextMonth = i + "";
					if (i < 10) {
						nextMonth = "0" + i;
					}
					upsertParams.put(BASIC_PAID_POINT_ + nextMonth, remainPointInfo.getOrDefault(BASIC_PAID_POINT,0)); // 충전
					upsertParams.put(BASIC_FREE_POINT_ + nextMonth,remainPointInfo.getOrDefault(BASIC_FREE_POINT,0)); // 적립
					upsertParams.put(END_PAID_POINT_ + nextMonth, remainPointInfo.getOrDefault(END_PAID_POINT,0));
					upsertParams.put(END_FREE_POINT_ + nextMonth, remainPointInfo.getOrDefault(END_FREE_POINT,0));
				}
				// endregion

				// region 액션 포인트 조회
				HashMap actionPointInfo = pointCalculateMapper.selectActionPoint(remainParam);
				if (actionPointInfo == null) {
					actionPointInfo = new HashMap();
				}

				upsertParams.put(CHARGE_PAID_POINT_ + mm, actionPointInfo.getOrDefault(CHARGE_PAID_POINT,0));
				upsertParams.put(CHARGE_FREE_POINT_ + mm, actionPointInfo.getOrDefault(CHARGE_FREE_POINT,0));
				upsertParams.put(USE_PAID_POINT_ + mm, actionPointInfo.getOrDefault(USE_PAID_POINT,0));
				upsertParams.put(USE_FREE_POINT_ + mm, actionPointInfo.getOrDefault(USE_FREE_POINT,0));
				// endregion

				// region 포인트 정산 upsert
				upsertParams.put("companyNo", companyNo);
				upsertParams.put("yyyy", yyyy);
				int upsertRow = upsertPointYearCalc(upsertParams);
				if(upsertRow == 1){
					successCompanyList.add(pointCalcTarget);
				}else{
					failCompanyList.add((pointCalcTarget));
				}
				// endregion
			}
		}
		// endregion

		pointCalculateResponse = PointCalculateResponse.builder()
				.successCount(successCompanyList.size())
				.successCompanyList(successCompanyList)
				.failCount(failCompanyList.size())
				.failCompanyList(failCompanyList)
				.totalCount(successCompanyList.size() + failCompanyList.size())
				.build();

		return pointCalculateResponse;
	}

	@Transactional(readOnly = false)
	public int upsertPointYearCalc(HashMap<String, Object> upsertParams) throws Exception{
		return pointCalculateMapper.upsertPointYearCalc(upsertParams);
	}
}
