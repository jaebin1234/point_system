package com.common.point.service;

import com.common.point.dao.mapper.PointMapper;
import com.common.point.exception.ErrorCode;
import com.common.point.exception.PointServerException;
import com.common.point.model.dto.Point;
import com.common.point.model.dto.PointChargeRequest;
import com.common.point.model.dto.PointChargeResponse;
import com.common.point.model.dto.PointHistory;
import com.common.point.model.vo.PUserVo;
import com.common.point.model.vo.PointHistoryVo;
import com.common.point.model.vo.PointVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PointChargeService {

	private final PointMapper pointMapper;

	@Transactional(readOnly = false)
	public PointChargeResponse postPointChargeCreateIF(PointChargeRequest pointChargeRequest) throws Exception {

		final String pointType = pointChargeRequest.getPointType();
		final String pointActionType = pointChargeRequest.getPointActionType();

		if(!"A".equals(pointActionType)){
			throw new PointServerException(ErrorCode.BAD_REQUEST_REQUEST_PARAM);
		}

		if(!"E".equals(pointType) && !"C".equals(pointType)){
			throw new PointServerException(ErrorCode.BAD_REQUEST_REQUEST_PARAM);
		}

		PointChargeResponse pointChargeResponse = postPointChargeCreate(pointChargeRequest);

		return pointChargeResponse;
	}

	private PointChargeResponse postPointChargeCreate(PointChargeRequest pointChargeRequest) throws Exception{
		final String pointType = pointChargeRequest.getPointType();
		final Integer companyNo = pointChargeRequest.getCompanyNo();
		final int chargePoint = pointChargeRequest.getPoint();

		Point beforePoint = pointMapper.selectPointByCompanyNo(companyNo);

		String pointGroupKey = generateUUID20();

		PointHistoryVo pointHistoryVo = PointHistoryVo.builder()
				.companyNo(companyNo)
				.userNo(pointChargeRequest.getUserNo())
				.pointType(pointChargeRequest.getPointType())
				.pointActionType(pointChargeRequest.getPointActionType())
				.point(chargePoint)
				.pointGroupKey(pointGroupKey)
				.description(pointChargeRequest.getDescription())
				.build();

		PointHistory pointHistory = pointMapper.insertPointHistory(pointHistoryVo);

		Date currentDate = pointHistory.getUpdateTimestamp();

		pointChargeRequest.setCurrentTimeStamp(currentDate);

		PointVo pointVo = new PointVo();

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

		PointChargeResponse pointChargeResponse = PointChargeResponse.builder()
				.pointHistoryNo(pointHistory.getPointHistoryNo())
				.build();

		return pointChargeResponse;
	}

	private String generateUUID20(){
		return UUID.randomUUID().toString().replace("-", "").substring(0, 20);
	}

}
