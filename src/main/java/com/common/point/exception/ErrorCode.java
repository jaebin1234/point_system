package com.common.point.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum ErrorCode {
    BAD_REQUEST_PARAM(400, "Bad Request Parameter"),
    NOT_ENOUGH_POINTS(400, "not enough points."),
    NOT_EXISTS_COMPANY_POINTS(400, "not exists company points."),
    POINT_UPDATE_FAIL(409, "Point update failed."),
    POINT_UPDATE_FAILED_AND_KAFKA_MESSAGE_RETRY_FAILED(500, "Point update failed and kafka retry failed."),
    POINT_UPDATE_FAILED_AND_KAFKA_MESSAGE_RETRY_SUCCESS(201, "Point update failed and kafka retry success."),
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