package com.common.point.dao.mapper;

import com.common.point.model.dto.Company;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface PointCalculateMapper {

	//ts_last_sync: 이전 5분 간격에서 10분 전의 동기화 시작 시간.
	//ts_last_sync_end: 이전 5분 간격에서 10분 후의 동기화 종료 시간.
	HashMap selectPointYearLastSyncTime();

	List<Company> selectPointCalcTargetList(HashMap targetParam) throws Exception;

	HashMap selectRemainingPoint(HashMap remainParam) throws Exception;

	HashMap selectActionPoint(HashMap remainParam) throws Exception;

	int upsertPointYearCalc(HashMap<String, Object> upsertParams) throws Exception;
}
