package com.m1a2st.simplebackendpractice.wallet.service;

import com.m1a2st.simplebackendpractice.user.po.UserProfile;
import com.m1a2st.simplebackendpractice.user.repository.UserProfileRepository;
import com.m1a2st.simplebackendpractice.wallet.dto.WalletRespDTO;
import com.m1a2st.simplebackendpractice.wallet.po.Wallet;
import com.m1a2st.simplebackendpractice.wallet.repository.WalletRepository;
import org.springframework.stereotype.Service;

/**
 * @Author m1a2st
 * @Date 2023/3/26
 * @Version v1.0
 */

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final UserProfileRepository userProfileRepository;

    public WalletService(WalletRepository walletRepository, UserProfileRepository userProfileRepository) {
        this.walletRepository = walletRepository;
        this.userProfileRepository = userProfileRepository;
    }

    public UserProfile queryByUsername(String username) {
        return userProfileRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not Found"));
    }

    public Wallet queryByUserId(Long userId){
        return walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Can't find wallet."));
    }

    public WalletRespDTO getWallet(String username) {
        UserProfile user = queryByUsername(username);
        Wallet wallet = queryByUserId(user.getId());
        return WalletRespDTO.builder()
                .userId(wallet.getUserId())
                .username(username)
                .balance(wallet.getBalance())
                .build();
    }
}
