package com.common.point.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Point {
	 // 포인트 고유번호
    private Integer pointNo;

    // 회사 고유번호
    private Integer companyNo;

    // 유상 포인트
    private Integer paidPoint;

    // 유상 포인트 충전 시간
    private LocalDateTime paidPointChargeTimestamp;

    // 유상 포인트 차감 시간
    private LocalDateTime paidPointUseTimestamp;

    // 무상 포인트
    private Integer freePoint;

    // 무상 포인트 충전 시간
    private LocalDateTime freePointChargeTimestamp;

    // 무상 포인트 차감 시간
    private LocalDateTime freePointUseTimestamp;

    // 생성시간
    private LocalDateTime insertTimestamp;

    // 수정시간
    private LocalDateTime updateTimestamp;
}
