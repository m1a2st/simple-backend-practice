package com.m1a2st.simplebackendpractice.wallet.service.Impl;

import com.m1a2st.simplebackendpractice.user.po.UserProfile;
import com.m1a2st.simplebackendpractice.user.repository.UserProfileRepository;
import com.m1a2st.simplebackendpractice.wallet.dto.WalletRecordDTO;
import com.m1a2st.simplebackendpractice.wallet.dto.WalletRespDTO;
import com.m1a2st.simplebackendpractice.wallet.enu.TransactionType;
import com.m1a2st.simplebackendpractice.wallet.po.Wallet;
import com.m1a2st.simplebackendpractice.wallet.po.WalletTransactionRecord;
import com.m1a2st.simplebackendpractice.wallet.repository.WalletRepository;
import com.m1a2st.simplebackendpractice.wallet.repository.WalletTransactionRecordRepository;
import com.m1a2st.simplebackendpractice.wallet.vo.WalletRecordVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.UUID;

import static java.lang.String.format;

/**
 * @Author m1a2st
 * @Date 2023/3/26
 * @Version v1.0
 */

@Service
public class WalletService implements com.m1a2st.simplebackendpractice.wallet.service.WalletService {

    private final WalletRepository walletRepository;
    private final UserProfileRepository userProfileRepository;
    private final WalletTransactionRecordRepository walletTxRepository;

    public WalletService(WalletRepository walletRepository, UserProfileRepository userProfileRepository, WalletTransactionRecordRepository walletTxRepository) {
        this.walletRepository = walletRepository;
        this.userProfileRepository = userProfileRepository;
        this.walletTxRepository = walletTxRepository;
    }

    @Override
    public UserProfile queryByUsername(String username) {
        return userProfileRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not Found"));
    }

    @Override
    public Wallet queryByUserId(Long userId) {
        return walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Can't find wallet."));
    }

    @Override
    public WalletRespDTO getWallet(String username) {
        UserProfile user = queryByUsername(username);
        Wallet wallet = queryByUserId(user.getId());
        return WalletRespDTO.builder()
                .userId(wallet.getUserId())
                .username(username)
                .balance(wallet.getBalance())
                .build();
    }

    @Override
    @Transactional(rollbackOn = {RuntimeException.class})
    public WalletRecordDTO deposit(String username, BigDecimal depositAmount) {
        UserProfile user = queryByUsername(username);
        Wallet wallet = queryByUserId(user.getId());
        BigDecimal beforeBalance = wallet.getBalance();
        BigDecimal afterBalance = beforeBalance.add(depositAmount);
        wallet.setBalance(afterBalance);
        walletRepository.save(wallet);
        walletTxRepository.save(WalletTransactionRecord.builder()
                .walletId(wallet.getId())
                .type(TransactionType.DEPOSIT)
                .operationUuid(UUID.randomUUID().toString())
                .beforeBalance(beforeBalance)
                .amount(depositAmount)
                .afterBalance(afterBalance)
                .memo(format("%s to %s", TransactionType.DEPOSIT.name(), wallet.getUserId()))
                .build());
        return WalletRecordDTO.builder()
                .fromUserId(wallet.getUserId())
                .beforeBalance(beforeBalance)
                .toUserId(wallet.getUserId())
                .afterBalance(afterBalance)
                .build();
    }

    @Override
    @Transactional(rollbackOn = {RuntimeException.class})
    public WalletRecordDTO withdraw(String username, BigDecimal withdrawAmount) {
        UserProfile user = queryByUsername(username);
        Wallet wallet = queryByUserId(user.getId());
        BigDecimal beforeBalance = wallet.getBalance();
        //  判斷存款是否小於提款
        if (beforeBalance.compareTo(withdrawAmount) < 0) {
            throw new RuntimeException("Your wallet balance is enough");
        }
        BigDecimal afterBalance = beforeBalance.subtract(withdrawAmount);
        wallet.setBalance(afterBalance);
        walletRepository.save(wallet);
        walletTxRepository.save(WalletTransactionRecord.builder()
                .walletId(wallet.getId())
                .type(TransactionType.WITHDRAW)
                .operationUuid(UUID.randomUUID().toString())
                .beforeBalance(beforeBalance)
                .amount(withdrawAmount)
                .afterBalance(afterBalance)
                .memo(format("%s from %s", TransactionType.WITHDRAW.name(), wallet.getUserId()))
                .build());
        return WalletRecordDTO.builder()
                .fromUserId(wallet.getUserId())
                .beforeBalance(beforeBalance)
                .toUserId(wallet.getUserId())
                .afterBalance(afterBalance)
                .build();
    }

    @Override
    @Transactional(rollbackOn = {RuntimeException.class})
    public WalletRecordDTO transfer(String username, Long toUserId, BigDecimal transferMount) {
        UserProfile user = queryByUsername(username);
        Wallet wallet = queryByUserId(user.getId());
        Wallet toWallet = queryByUserId(toUserId);
        BigDecimal beforeBalance = wallet.getBalance();
        if (beforeBalance.compareTo(transferMount) < 0) {
            throw new RuntimeException("Your wallet balance is enough");
        }

        BigDecimal afterBalance = beforeBalance.subtract(transferMount);
        wallet.setBalance(afterBalance);
        walletRepository.save(wallet);
        walletTxRepository.save(WalletTransactionRecord.builder()
                .walletId(wallet.getId())
                .type(TransactionType.TRANSFER_TO)
                .operationUuid(UUID.randomUUID().toString())
                .beforeBalance(beforeBalance)
                .amount(transferMount)
                .afterBalance(afterBalance)
                .memo(format("%s %s", TransactionType.TRANSFER_TO.name(), wallet.getUserId()))
                .build());

        BigDecimal toWalletBeforeBalance = toWallet.getBalance();
        BigDecimal toWalletAfterBalance = toWalletBeforeBalance.add(transferMount);
        toWallet.setBalance(toWalletAfterBalance);
        walletRepository.save(toWallet);
        walletTxRepository.save(WalletTransactionRecord.builder()
                .walletId(toWallet.getId())
                .type(TransactionType.TRANSFER_FROM)
                .operationUuid(UUID.randomUUID().toString())
                .beforeBalance(toWalletBeforeBalance)
                .amount(transferMount)
                .afterBalance(toWalletAfterBalance)
                .memo(format("%s %s", TransactionType.TRANSFER_FROM.name(), toWallet.getUserId()))
                .build());
        return WalletRecordDTO.builder()
                .fromUserId(wallet.getUserId())
                .beforeBalance(beforeBalance)
                .toUserId(toWallet.getUserId())
                .afterBalance(afterBalance)
                .build();
    }

    @Override
    public Page<WalletRecordVO> getWalletRecord(String username, Integer page, Integer size) {
        UserProfile user = queryByUsername(username);
        return walletTxRepository.findAllByUsername(user.getId(), PageRequest.of(page, size));
    }
}
