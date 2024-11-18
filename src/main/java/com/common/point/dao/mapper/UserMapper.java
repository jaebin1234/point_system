package com.common.point.dao.mapper;

import com.common.point.model.vo.PUserVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
	int insertPUser(PUserVo user);
	void deleteAllUsers();
}
