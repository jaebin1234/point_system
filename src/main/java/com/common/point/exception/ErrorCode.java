package com.common.point.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum ErrorCode {
    BAD_REQUEST_COMPANY_PARAM(400, "Bad Request");

    private final int status;
    private final String message;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}