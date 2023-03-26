package com.m1a2st.simplebackendpractice.wallet.repository;

import com.m1a2st.simplebackendpractice.user.po.UserProfile;
import com.m1a2st.simplebackendpractice.wallet.po.Wallet;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.util.Optional;

/**
 * @Author m1a2st
 * @Date 2023/3/24
 * @Version v1.0
 */
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    // 這邊不使用join的原因是，使用lock加上join時，兩個table都會被鎖住，影響效能
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Wallet> findByUserId(Long userId);

}
