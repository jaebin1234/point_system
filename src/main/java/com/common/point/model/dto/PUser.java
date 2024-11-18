package com.common.point.model.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PUser {

     // 사용자 고유번호
    private Integer userNo;

    // 사용자 이름
    private String userName;

    // 생성시간
    private Date insertTimestamp;

    // 수정시간
    private Date updateTimestamp;
}
