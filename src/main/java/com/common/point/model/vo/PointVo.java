package com.common.point.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PointVo {
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


}
