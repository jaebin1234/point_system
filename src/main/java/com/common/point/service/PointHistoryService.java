package com.common.point.service;

import com.common.point.dao.mapper.PointMapper;
import com.common.point.exception.ErrorCode;
import com.common.point.exception.PointServerException;
import com.common.point.model.dto.*;
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
public class PointHistoryService {

	private final PointMapper pointMapper;

	@Transactional(readOnly = true)
	public List<PointHistory> getPointHistoryList(PointHistoryRequest pointHistoryRequest) throws Exception {

		List<PointHistory> pointHistoryList = pointMapper.selectPointHistoryList(pointHistoryRequest);

		return pointHistoryList;
	}

	@Transactional(readOnly = true)
	public boolean getPointHistoryListExists(PointHistoryRequest pointHistoryRequest) throws Exception {

		List<PointHistory> pointHistoryList = pointMapper.selectPointHistoryList(pointHistoryRequest);

		if(pointHistoryList.isEmpty()){
			return false;
		}else{
			return true;
		}
	}

}
