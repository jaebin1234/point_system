package com.common.point.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointUseFailMessage {

	private Integer companyNo;
	private Integer userNo;
	private String pointActionType;
	private String description;
	private int point;
	private long timestamp;
	private String pointGroupKey;

}
