package com.common.point.dao.mapper;

import com.common.point.model.dto.PUser;
import com.common.point.model.vo.PUserVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
	List<PUser> selectPUserList();
	int insertPUser(PUserVo user);
	void deleteAllUsers();
}
