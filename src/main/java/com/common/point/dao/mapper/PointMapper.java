package com.common.point.dao.mapper;

import com.common.point.model.dto.Company;
import com.common.point.model.vo.CompanyVo;
import com.common.point.model.vo.PointVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PointMapper {
	int insertPoint(PointVo pointVo) throws Exception;
	void deleteAllPoints() throws Exception;
}
