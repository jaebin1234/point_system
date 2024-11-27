package com.common.point.util;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Component
public class Utils {

	public String generateUUID20() throws Exception{
		return UUID.randomUUID().toString().replace("-", "").substring(0, 20);
	}

	public long localDateTimeToLong(LocalDateTime localDateTime) {
		return localDateTime.toEpochSecond(ZoneOffset.UTC) * 1_000_000_000L + localDateTime.getNano();
	}

	public LocalDateTime longToLocalDateTime(long unixTime){
		long epochSecond = unixTime / 1_000_000_000L; // 초 단위
        int nanoAdjustment = (int) (unixTime % 1_000_000_000L); // 나노초 단위

		LocalDateTime localDateTime = Instant.ofEpochSecond(epochSecond, nanoAdjustment)
                                        .atZone(ZoneOffset.UTC)
                                        .toLocalDateTime();

		return localDateTime;
	}
}
