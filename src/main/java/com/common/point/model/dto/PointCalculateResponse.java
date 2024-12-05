package com.common.point.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class PointCalculateResponse {
	private int successCount;
	private List<Company> successCompanyList;

	private int failCount;
	private List<Company> failCompanyList;

	private int totalCount;

}
