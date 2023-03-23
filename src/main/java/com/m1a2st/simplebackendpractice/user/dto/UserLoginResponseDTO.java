package com.m1a2st.simplebackendpractice.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author m1a2st
 * @Date 2023/3/23
 * @Version v1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginResponseDTO {

    @Schema(description = "由誰建立的")
    private String createBy;
    @Schema(description = "創造日期")
    private Date createDate;
    @Schema(description = "最後更新日期")
    private Date lastModifiedDate;
    @Schema(description = "登入時間")
    private Date loginTime;
}
