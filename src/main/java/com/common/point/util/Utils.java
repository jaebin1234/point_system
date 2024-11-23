package com.common.point.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class Utils {

	public String generateUUID20() throws Exception{
		return UUID.randomUUID().toString().replace("-", "").substring(0, 20);
	}
}
