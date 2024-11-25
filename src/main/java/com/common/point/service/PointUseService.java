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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PointUseService {

	private final PointMapper pointMapper;
	private final Utils utils;

	@Transactional(readOnly = false)
	public PointChargeAndUseResponse postPointUseIF(PointChargeAndUseRequest pointChargeAndUseRequest) throws Exception {
		final String pointType = pointChargeAndUseRequest.getPointType();
		final String pointActionType = pointChargeAndUseRequest.getPointActionType();

		if (!"U".equals(pointActionType)) {
			throw new PointServerException(ErrorCode.BAD_REQUEST_PARAM);
		}

		PointChargeAndUseResponse pointChargeAndUseResponse = postPointUse(pointChargeAndUseRequest);

		return pointChargeAndUseResponse;
	}

	private PointChargeAndUseResponse postPointUse(PointChargeAndUseRequest pointChargeAndUseRequest) throws Exception {
		final String pointActionType = pointChargeAndUseRequest.getPointActionType();
		final Integer companyNo = pointChargeAndUseRequest.getCompanyNo();
		final Integer userNo = pointChargeAndUseRequest.getUserNo();
		int usePoint = pointChargeAndUseRequest.getPoint();

		List<Integer> pointHistoryNoList =  new ArrayList<>();
		LocalDateTime currentDate = LocalDateTime.now();

		log.error("postPointUse start Thread=" + Thread.currentThread().getName());
		Point beforePoint = pointMapper.selectPointByCompanyNo(companyNo);

		if(beforePoint == null){
			throw new PointServerException(ErrorCode.NOT_EXISTS_COMPANY_POINTS);
		}

		// 동시성 문제 테스트 예시 코드
		if(userNo == 33){
			log.error("Thread=" + Thread.currentThread().getName() + " sleep");
			Thread.sleep(2000);
		}

		String pointGroupKey = utils.generateUUID20();
		final int beforePaidPoint = beforePoint.getPaidPoint();
		final int beforeFreePoint = beforePoint.getFreePoint();
		final LocalDateTime beforeUpdateTimestamp = beforePoint.getUpdateTimestamp();

		log.error("Thread=" + Thread.currentThread().getName() + " companyNo= "	+ companyNo + " userNo=" + userNo + " usePoint=" + usePoint + " beforePaidPoint=" + beforePaidPoint + " beforeFreePoint=" + beforeFreePoint);

		final int totalPoint = beforePaidPoint + beforeFreePoint;

		if(totalPoint < usePoint){
			throw new PointServerException(ErrorCode.NOT_ENOUGH_POINTS);
		}

		boolean isOnlyUseFreePoint = beforeFreePoint - usePoint > 0;
		final int currentFreePoint = isOnlyUseFreePoint ? beforeFreePoint - usePoint : 0;

		if (beforeFreePoint > 0) {
			final int thisFreeUsePoint = isOnlyUseFreePoint ? usePoint : beforeFreePoint;

			PointHistoryVo freePointUseHistoryVo = PointHistoryVo.builder()
					.companyNo(companyNo)
					.userNo(userNo)
					.pointType("E")
					.pointActionType(pointActionType)
					.point(thisFreeUsePoint)
					.pointGroupKey(pointGroupKey)
					.description(pointChargeAndUseRequest.getDescription())
					.insertTimestamp(currentDate)
					.updateTimestamp(currentDate)
					.build();

			PointHistory freePointUseHistory = pointMapper.insertPointHistory(freePointUseHistoryVo);
			pointHistoryNoList.add(freePointUseHistory.getPointHistoryNo());

			usePoint = isOnlyUseFreePoint ? 0 : usePoint - beforeFreePoint;
			log.error("Thread=" + Thread.currentThread().getName() + " after use free point" + " companyNo= "	+ companyNo + " userNo=" + userNo + " usePoint=" + usePoint + " beforePaidPoint=" + beforePaidPoint + " beforeFreePoint=" + beforeFreePoint);
		}


		final int currentPaidPoint = beforePaidPoint - usePoint ;

		if(usePoint > 0){
			PointHistoryVo paidPointUseHistoryVo = PointHistoryVo.builder()
					.companyNo(companyNo)
					.userNo(userNo)
					.pointType("C")
					.pointActionType(pointActionType)
					.point(usePoint)
					.pointGroupKey(pointGroupKey)
					.description(pointChargeAndUseRequest.getDescription())
					.insertTimestamp(currentDate)
					.updateTimestamp(currentDate)
					.build();

			PointHistory paidPointUseHistory = pointMapper.insertPointHistory(paidPointUseHistoryVo);
			pointHistoryNoList.add(paidPointUseHistory.getPointHistoryNo());
			log.error( "Thread=" + Thread.currentThread().getName() + " after use paid point" + " companyNo= "	+ companyNo + " userNo=" + userNo + " usePoint=" + usePoint + " beforePaidPoint=" + beforePaidPoint + " beforeFreePoint=" + beforeFreePoint);
		}

		PointVo pointVo = new PointVo();

		if (isOnlyUseFreePoint) {
			// 무상 포인트만 사용
			pointVo = PointVo.builder()
					.companyNo(companyNo)
					.freePoint(currentFreePoint)
					.freePointUseTimestamp(currentDate)
					.beforePaidPoint(beforePaidPoint)
					.beforeFreePoint(beforeFreePoint)
					.beforeUpdateTimestamp(beforeUpdateTimestamp)
					.build();
		} else if (beforeFreePoint <= 0) {
			// 유상 포인트만 사용
			pointVo = PointVo.builder()
					.companyNo(companyNo)
					.paidPoint(currentPaidPoint)
					.paidPointUseTimestamp(currentDate)
					.beforePaidPoint(beforePaidPoint)
					.beforeFreePoint(beforeFreePoint)
					.beforeUpdateTimestamp(beforeUpdateTimestamp)
					.build();
		} else {
			// 무상 포인트와 유상 포인트를 모두 사용
			pointVo = PointVo.builder()
					.companyNo(companyNo)
					.freePoint(currentFreePoint)
					.freePointUseTimestamp(currentDate)
					.paidPoint(currentPaidPoint)
					.paidPointUseTimestamp(currentDate)
					.beforePaidPoint(beforePaidPoint)
					.beforeFreePoint(beforeFreePoint)
					.beforeUpdateTimestamp(beforeUpdateTimestamp)
					.build();
		}

		final int updateRow = pointMapper.updatePoint(pointVo);

		if(updateRow != 1){
			// 낙관적 락
			throw new PointServerException(ErrorCode.POINT_UPDATE_FAIL);
		}

		PointChargeAndUseResponse pointChargeAndUseResponse = PointChargeAndUseResponse.builder()
				.pointHistoryNoList(pointHistoryNoList)
				.build();

		log.error("Thread=" + Thread.currentThread().getName() + " success use point" + " companyNo= "	+ companyNo + " userNo=" + userNo + " usePoint=" + usePoint + " beforePaidPoint=" + beforePaidPoint + " beforeFreePoint=" + beforeFreePoint);

		return pointChargeAndUseResponse;
	}

}
