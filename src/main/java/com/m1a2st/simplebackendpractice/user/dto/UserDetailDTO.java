package com.m1a2st.simplebackendpractice.user.dto;

import com.m1a2st.simplebackendpractice.user.enu.UserStatus;
import com.m1a2st.simplebackendpractice.user.po.UserProfile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

/**
 * @Author m1a2st
 * @Date 2023/3/23
 * @Version v1.0
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
public class UserDetailDTO {

    @Schema(description = "使用者編號")
    private Long id;
    @Schema(description = "使用者名稱")
    private String username;
    @Schema(description = "使用者狀態")
    private UserStatus status;
    @Schema(description = "創造日期")
    private Date createDate;

    public UserDetailDTO toDTO(UserProfile userProfile){
        return UserDetailDTO.builder()
                .id(userProfile.getId())
                .username(userProfile.getUsername())
                .status(userProfile.getStatus())
                .createDate(userProfile.getCreateDate())
                .build();
    }
}
