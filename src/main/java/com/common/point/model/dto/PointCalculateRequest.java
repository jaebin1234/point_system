package com.common.point.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PointCalculateRequest {

	private Integer companyNo;
	private String companyRegNo;
	private String yyyymm;
	private boolean isRealTimeCalculation;

}
