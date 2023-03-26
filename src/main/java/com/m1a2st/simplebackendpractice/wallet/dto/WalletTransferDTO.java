package com.m1a2st.simplebackendpractice.wallet.dto;

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
public class WalletTransferDTO {

    private Long toUserId;

    private BigDecimal amount;
}
