package com.common.point.exception;

import com.common.point.model.dto.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = {"com.common.point.controller", "com.common.point.test"})
public class GlobalExceptionHandler {

	@ExceptionHandler({PointServerException.class})
	public ResponseEntity<ErrorResponseDTO> PointServerBadRequestException(PointServerException e) {
		return CustomExceptionReturn.returnException(e);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException e) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(400, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<ErrorResponseDTO> handleGeneralNullPointerException(NullPointerException e) {
		ErrorResponseDTO errorResponse = new ErrorResponseDTO(500, e.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponseDTO> handleGeneralException(Exception e) {
		ErrorResponseDTO errorResponse = new ErrorResponseDTO(500, e.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}