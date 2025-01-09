package com.common.point.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PointCalculateRequest {

	private Integer companyNo;
	private String companyRegNo;
	private String yyyymm;
	private boolean isRealTimeCalculation;

}
