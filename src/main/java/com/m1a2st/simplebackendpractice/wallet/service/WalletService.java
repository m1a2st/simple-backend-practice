package com.m1a2st.simplebackendpractice.wallet.service;

import com.m1a2st.simplebackendpractice.user.po.UserProfile;
import com.m1a2st.simplebackendpractice.wallet.dto.WalletRecordDTO;
import com.m1a2st.simplebackendpractice.wallet.dto.WalletRespDTO;
import com.m1a2st.simplebackendpractice.wallet.po.Wallet;
import com.m1a2st.simplebackendpractice.wallet.vo.WalletRecordVO;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

/**
 * @Author m1a2st
 * @Date 2023/3/26
 * @Version v1.0
 */
public interface WalletService {

    UserProfile queryByUsername(String username);

    Wallet queryByUserId(Long userId);

    WalletRespDTO getWallet(String username);

    WalletRecordDTO deposit(String username, BigDecimal depositAmount);

    WalletRecordDTO withdraw(String username, BigDecimal withdrawAmount);

    WalletRecordDTO transfer(String username, Long toUserId, BigDecimal transferMount);

    Page<WalletRecordVO> getWalletRecord(String username, Integer page, Integer size);
}
