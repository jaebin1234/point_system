package com.common.point.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PointUseFailMessage {

	private Integer companyNo;
	private Integer userNo;
	private String pointActionType;
	private String description;
	private int point;
	private long beforeUpdateTimestamp;
	private long updateTimestamp;
	private String pointGroupKey;

}
