package com.common.point.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Date;

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
    private Date paidPointChargeTimestamp;

    // 유상 포인트 차감 시간
    private Date paidPointUseTimestamp;

    // 무상 포인트
    private Integer freePoint;

    // 무상 포인트 충전 시간
    private Date freePointChargeTimestamp;

    // 무상 포인트 차감 시간
    private Date freePointUseTimestamp;

    // 생성시간
    private Date insertTimestamp;

    // 수정시간
    private Date updateTimestamp;
}
