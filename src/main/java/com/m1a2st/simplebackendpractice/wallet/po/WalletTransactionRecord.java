package com.m1a2st.simplebackendpractice.wallet.po;

import com.m1a2st.simplebackendpractice.wallet.enu.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
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
@Entity
@Table(name = "wallet_transaction_record")
@EntityListeners(AuditingEntityListener.class)
public class WalletTransactionRecord implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long walletId;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private String operationUuid;

    private BigDecimal beforeBalance;

    private BigDecimal amount;

    private BigDecimal afterBalance;

    private String memo;

    @CreatedDate
    private Date createDate;

    @CreatedBy
    private String createBy;

    @LastModifiedDate
    private Date lastModifiedDate;

    @LastModifiedBy
    private String lastModifiedBy;

}
