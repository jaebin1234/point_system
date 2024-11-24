package com.common.point.service;

import com.common.point.dao.mapper.PointMapper;
import com.common.point.exception.ErrorCode;
import com.common.point.exception.PointServerException;
import com.common.point.model.dto.Point;
import com.common.point.model.dto.PointChargeAndUseRequest;
import com.common.point.model.dto.PointChargeAndUseResponse;
import com.common.point.model.dto.PointHistory;
import com.common.point.model.vo.PointHistoryVo;
import com.common.point.model.vo.PointVo;
import com.common.point.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PointChargeService {

	private final PointMapper pointMapper;
	private final Utils utils;

	@Transactional(readOnly = false)
	public PointChargeAndUseResponse postPointChargeCreateIF(PointChargeAndUseRequest pointChargeAndUseRequest) throws Exception {

		final String pointType = pointChargeAndUseRequest.getPointType();
		final String pointActionType = pointChargeAndUseRequest.getPointActionType();

		if(!"A".equals(pointActionType)){
			throw new PointServerException(ErrorCode.BAD_REQUEST_PARAM);
		}

		if(!"E".equals(pointType) && !"C".equals(pointType)){
			throw new PointServerException(ErrorCode.BAD_REQUEST_PARAM);
		}

		PointChargeAndUseResponse pointChargeAndUseResponse = postPointChargeCreate(pointChargeAndUseRequest);

		return pointChargeAndUseResponse;
	}

	private PointChargeAndUseResponse postPointChargeCreate(PointChargeAndUseRequest pointChargeAndUseRequest) throws Exception{
		final String pointType = pointChargeAndUseRequest.getPointType();
		final Integer companyNo = pointChargeAndUseRequest.getCompanyNo();
		final int chargePoint = pointChargeAndUseRequest.getPoint();

		Point beforePoint = pointMapper.selectPointByCompanyNo(companyNo);

		if(beforePoint == null){
			throw new PointServerException(ErrorCode.NOT_EXISTS_COMPANY_POINTS);
		}

		String pointGroupKey = utils.generateUUID20();

		PointHistoryVo pointHistoryVo = PointHistoryVo.builder()
				.companyNo(companyNo)
				.userNo(pointChargeAndUseRequest.getUserNo())
				.pointType(pointChargeAndUseRequest.getPointType())
				.pointActionType(pointChargeAndUseRequest.getPointActionType())
				.point(chargePoint)
				.pointGroupKey(pointGroupKey)
				.description(pointChargeAndUseRequest.getDescription())
				.build();

		PointHistory pointHistory = pointMapper.insertPointHistory(pointHistoryVo);

		List<Integer> pointHistoryNoList =  new ArrayList<>();
		pointHistoryNoList.add(pointHistory.getPointHistoryNo());

		Date currentDate = pointHistory.getUpdateTimestamp();

		pointChargeAndUseRequest.setCurrentTimeStamp(currentDate);

		PointVo pointVo = new PointVo();

		// 예시로 paidPoint를 0아래로 만들어서 익셉션 내보기
		//chargePoint = -1000;

		if("C".equals(pointType)){
			pointVo = PointVo.builder()
					.paidPoint(beforePoint.getPaidPoint() + chargePoint)
					.paidPointChargeTimestamp(currentDate)
					.companyNo(companyNo)
					.build();
		}else { // "E".equals(pointType)
			pointVo = PointVo.builder()
					.freePoint(beforePoint.getFreePoint() + chargePoint)
					.freePointChargeTimestamp(currentDate)
					.companyNo(companyNo)
					.build();
		}

		final int updateRow = pointMapper.updatePoint(pointVo);

		if(updateRow != 1){
			throw new PointServerException(ErrorCode.POINT_UPDATE_FAIL);
		}

		PointChargeAndUseResponse pointChargeAndUseResponse = PointChargeAndUseResponse.builder()
				.pointHistoryNoList(pointHistoryNoList)
				.build();

		return pointChargeAndUseResponse;
	}

}
