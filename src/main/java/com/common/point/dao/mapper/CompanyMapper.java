package com.common.point.dao.mapper;

import com.common.point.model.dto.Company;
import com.common.point.model.vo.CompanyVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface  CompanyMapper {
	List<Company> selectCompanyList();

	int insertCompany(CompanyVo companyVo);

	void deleteAllCompanies();
}
