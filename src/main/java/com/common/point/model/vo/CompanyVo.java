package com.common.point.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyVo {

    // 회사 고유번호
    private Integer companyNo;

    // 회사명
    private String companyName;

    // 사업자 번호
    private String companyRegNo;

    // 생성시간
    private Date insertTimestamp;

    // 수정시간
    private Date updateTimestamp;

}