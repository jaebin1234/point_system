package com.common.point.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PUserVo {

   // 사용자 고유번호
    private Integer userNo;

    // 사용자 이름
    private String userName;

}