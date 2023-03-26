package com.m1a2st.simplebackendpractice.wallet.controller;

import com.m1a2st.simplebackendpractice.wallet.dto.WalletRequestDTO;
import com.m1a2st.simplebackendpractice.wallet.dto.WalletRecordDTO;
import com.m1a2st.simplebackendpractice.wallet.dto.WalletRespDTO;
import com.m1a2st.simplebackendpractice.wallet.dto.WalletTransferDTO;
import com.m1a2st.simplebackendpractice.wallet.service.Impl.WalletService;
import com.m1a2st.simplebackendpractice.wallet.vo.WalletRecordVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @Author m1a2st
 * @Date 2023/3/26
 * @Version v1.0
 */
@Tag(name = "Wallet")
@RestController
@RequestMapping("/api/v1.0")
@SecurityRequirement(name = "Authentication")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @Operation(summary = "查詢錢包餘額")
    @PreAuthorize("hasRole('DEFAULT')")
    @GetMapping("/wallet")
    public WalletRespDTO showWallet(Principal principal) {
        return walletService.getWallet(principal.getName());
    }

    @Operation(summary = "存款")
    @PreAuthorize("hasRole('DEFAULT')")
    @PostMapping("/wallet:deposit")
    public WalletRecordDTO deposit(
            Principal principal,
            @RequestBody WalletRequestDTO walletRequestDTO) {
        return walletService.deposit(principal.getName(), walletRequestDTO.getAmount());
    }

    @Operation(summary = "提款")
    @PreAuthorize("hasRole('DEFAULT')")
    @PostMapping("/wallet:withdraw")
    public WalletRecordDTO withdraw(
            Principal principal,
            @RequestBody WalletRequestDTO walletRequestDTO) {
        return walletService.withdraw(principal.getName(), walletRequestDTO.getAmount());
    }

    @Operation(summary = "匯款")
    @PreAuthorize("hasRole('DEFAULT')")
    @PostMapping("/wallet:transfer")
    public WalletRecordDTO transfer(
            Principal principal,
            @RequestBody WalletTransferDTO walletTransferDTO) {
        return walletService.transfer(principal.getName(), walletTransferDTO.getToUserId(), walletTransferDTO.getAmount());
    }

    @Operation(summary = "查詢錢包交易紀錄")
    @PreAuthorize("hasRole('DEFAULT')")
    @GetMapping("/wallet/record")
    public Page<WalletRecordVO> getWalletRecord(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            Principal principal) {
        return walletService.getWalletRecord(principal.getName(), page, size);
    }


}
