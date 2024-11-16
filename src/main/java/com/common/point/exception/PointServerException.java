package com.common.point.exception;

import lombok.Getter;

@Getter
public class PointServerException extends RuntimeException {
	private ErrorCode errorCode;

	public PointServerException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
        return errorCode;
    }
}