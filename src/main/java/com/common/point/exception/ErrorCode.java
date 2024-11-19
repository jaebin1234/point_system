package com.common.point.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum ErrorCode {
    BAD_REQUEST_REQUEST_PARAM(400, "Bad Request Parameter"),
    NO_POINT_LEFT(400, "There are no points left."),
    POINT_UPDATE_FAIL(201, "Point update failed."),
    SERVER_ERROR(500, "Server Error");

    private final int status;
    private final String message;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}