package com.m1a2st.simplebackendpractice.user.dto;

import com.m1a2st.simplebackendpractice.user.po.UserProfile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author m1a2st
 * @Date 2023/3/19
 * @Version v1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileReqDTO {

    @Schema(description = "使用者名稱")
    private String username;
    @Schema(description = "使用者密碼")
    private String password;
    @Schema(description = "密碼重複輸入驗證用")
    private String repeatPassword;
}
