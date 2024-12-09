package com.common.point.service;

import com.common.point.dao.mapper.PointMapper;
import com.common.point.model.dto.PointHistory;
import com.common.point.model.dto.PointHistoryRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
