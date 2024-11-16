package com.common.point.exception;

import com.common.point.model.dto.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
public class CustomExceptionReturn {
	public static ResponseEntity<ErrorResponseDTO> returnException(PointServerException e) {

		log.error("{} : {}", new Object[]{e.getErrorCode(), e.getMessage()});
		ErrorCode errorCode = e.getErrorCode();
		return new ResponseEntity<>(new ErrorResponseDTO(errorCode.getStatus(), errorCode.getMessage()), HttpStatus.valueOf(errorCode.getStatus()));
	}
}