package com.m1a2st.simplebackendpractice.wallet.repository;

import com.m1a2st.simplebackendpractice.wallet.po.WalletTransactionRecord;
import com.m1a2st.simplebackendpractice.wallet.vo.WalletRecordVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WalletTransactionRecordRepository extends JpaRepository<WalletTransactionRecord, Long> {


    @Query("SELECT " +
            "new com.m1a2st.simplebackendpractice.wallet.vo.WalletRecordVO(wt.id, u.username, wt.type, wt.operationUuid, wt.beforeBalance, wt.amount, wt.afterBalance, wt.memo, wt.createDate, wt.createBy, wt.lastModifiedDate, wt.lastModifiedBy) " +
            "FROM WalletTransactionRecord wt " +
            "INNER JOIN Wallet w ON w.id = wt.walletId " +
            "INNER JOIN UserProfile u ON w.userId = u.id " +
            "WHERE u.id = :userId " +
            "ORDER BY wt.createDate DESC")
    Page<WalletRecordVO> findAllByUsername(Long userId, Pageable pageable);
}
