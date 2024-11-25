package com.common.point.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PointChargeAndUseRequest {

	@NotNull
	private Integer companyNo;
	@NotNull
	private Integer userNo;
	@PositiveOrZero
	private int point;
	private String pointType;
	private String pointActionType;
	private String description;

	private LocalDateTime currentTimeStamp;

	public void setCurrentTimeStamp(LocalDateTime currentTimeStamp) {
		this.currentTimeStamp = currentTimeStamp;
	}

}
