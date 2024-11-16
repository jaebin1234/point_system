package com.common.point.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Company {
	// 회사 고유번호
    private Integer companyNo;

    // 회사명
    private String companyName;

    // 사업자 번호
    private String companyRegNo;

}
