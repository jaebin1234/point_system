package com.common.point.service;

import com.common.point.dao.mapper.CompanyMapper;
import com.common.point.dao.mapper.PointMapper;
import com.common.point.model.dto.Company;
import com.common.point.model.vo.CompanyVo;
import com.common.point.model.vo.PointVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PointService {

	private final PointMapper pointMapper;

	public int createPoint(int companyNo) throws Exception {

		PointVo pointVo = PointVo.builder()
				.companyNo(companyNo)
				.build();


		int insertPointNo = pointMapper.insertPoint(pointVo);

		return insertPointNo;
	}

	public void deleteAllPoints() throws Exception {
		pointMapper.deleteAllPoints();
	}

}
