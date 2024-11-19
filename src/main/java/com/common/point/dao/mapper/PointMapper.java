package com.common.point.dao.mapper;

import com.common.point.model.dto.Point;
import com.common.point.model.dto.PointHistory;
import com.common.point.model.vo.PointHistoryVo;
import com.common.point.model.vo.PointVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PointMapper {
	Point selectPointByCompanyNo(Integer companyNo)throws Exception;
	int insertPoint(PointVo pointVo) throws Exception;
	void deleteAllPoints() throws Exception;
	PointHistory insertPointHistory(PointHistoryVo pointHistoryVo) throws Exception;
	int updatePoint(PointVo pointVo) throws Exception;
}
