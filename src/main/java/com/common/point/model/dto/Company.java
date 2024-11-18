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
public class Company {
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

    public Company(Integer companyNo, String companyName, String companyRegNo){
        this.companyNo = companyNo;
        this.companyName = companyName;
        this.companyRegNo = companyRegNo;
    }
}
