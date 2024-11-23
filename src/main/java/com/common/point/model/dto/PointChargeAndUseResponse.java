package com.common.point.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PointChargeAndUseResponse {

	@NotNull
	private Integer companyNo;
	@NotNull
	private Integer userNo;
	private Integer pointNo;
	private List<Integer> pointHistoryNoList;
	private int point;

}
