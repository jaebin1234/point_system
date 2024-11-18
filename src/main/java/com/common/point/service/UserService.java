package com.common.point.service;

import com.common.point.dao.mapper.UserMapper;
import com.common.point.model.vo.PUserVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
	private final UserMapper userMapper;

	@Transactional(readOnly = false)
	public int postCreateUser(String userName) throws Exception {

		PUserVo pUserVo = PUserVo.builder()
                .userName(userName)
                .build();

		int insertUserNo = userMapper.insertPUser(pUserVo);

		return insertUserNo;
	}

	@Transactional(readOnly = false)
	public void postDeleteAllUser() throws Exception {

		userMapper.deleteAllUsers();

	}

}
