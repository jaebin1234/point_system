package com.common.point.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PointChargeResponse {

	@NotNull
	private Integer companyNo;
	@NotNull
	private Integer userNo;
	private Integer pointNo;
	private Integer pointHistoryNo;
	private int point;

	public PointChargeResponse(Integer pointHistoryNo){
		this.pointHistoryNo = pointHistoryNo;
	}

}
