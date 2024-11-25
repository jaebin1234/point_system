package com.common.point.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PointHistory {
	 // 포인트 히스토리 고유번호
    private Integer pointHistoryNo;

    // 회사 고유번호
    private Integer companyNo;

    // 액션 사용자 고유번호
    private Integer userNo;

    // 포인트 타입 C:유상, E:무상
    private String pointType;

    // 포인트 액션 타입 A:충전, U:사용, C:사용취소, X:소멸, T:전환한, B:전환받은, R:환불
    private String pointActionType;

    // 액션 포인트
    private Integer point;

    // 포인트 액션 그룹키
    private String pointGroupKey;

    // 포인트 사용 취소 그룹키
    private String pointCancelGroupKey;

    // 포인트 액션 내용
    private String description;

    // 생성시간
    private LocalDateTime insertTimestamp;

    // 수정시간
    private LocalDateTime updateTimestamp;
}
