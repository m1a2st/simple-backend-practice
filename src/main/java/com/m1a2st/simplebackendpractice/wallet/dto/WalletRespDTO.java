package com.m1a2st.simplebackendpractice.wallet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author m1a2st
 * @Date 2023/3/26
 * @Version v1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WalletRespDTO {

    @Schema(description = "使用者帳號")
    private Long userId;

    @Schema(description = "使用者名稱")
    private String username;

    @Schema(description = "餘額")
    private BigDecimal balance;
}
