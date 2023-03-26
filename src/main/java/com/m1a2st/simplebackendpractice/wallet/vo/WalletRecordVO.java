package com.m1a2st.simplebackendpractice.wallet.vo;

import com.m1a2st.simplebackendpractice.wallet.enu.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author m1a2st
 * @Date 2023/3/26
 * @Version v1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WalletRecordVO {

    private Long id;

    private String username;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private String operationUuid;

    private BigDecimal beforeBalance;

    private BigDecimal amount;

    private BigDecimal afterBalance;

    private String memo;

    private Date createDate;

    private String createBy;

    private Date lastModifiedDate;

    private String lastModifiedBy;
}
