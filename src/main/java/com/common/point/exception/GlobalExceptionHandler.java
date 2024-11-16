package com.common.point.exception;

import com.common.point.model.dto.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler({PointServerException.class})
	public ResponseEntity<ErrorResponseDTO> PointServerBadRequestException(PointServerException e) {
		return CustomExceptionReturn.returnException(e);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponseDTO> handleGeneralException(Exception e) {
		// 500 Internal Server Error
		ErrorResponseDTO errorResponse = new ErrorResponseDTO(500, e.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}