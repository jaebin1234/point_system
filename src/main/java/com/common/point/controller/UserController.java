package com.common.point.controller;

import com.common.point.model.dto.Response;
import com.common.point.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

	private final UserService userService;

	@PostMapping("/create")
	public ResponseEntity<Response<Integer>> postCreateUser(@RequestParam("userName") String userName) throws Exception {
		final int createUserNo = userService.postCreateUser(userName);
		Response<Integer> response = Response.<Integer>builder()
				.status(200)
				.message("Success")
				.data(createUserNo)
				.build();

		return ResponseEntity.ok(response);
	}

  	@PostMapping("/delete/all")
    public ResponseEntity<String> postDeleteAllUser() throws Exception{

		userService.postDeleteAllUser();

        return ResponseEntity.ok("All users deleted");
    }

}
