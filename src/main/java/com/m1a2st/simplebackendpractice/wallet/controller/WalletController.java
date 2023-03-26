package com.m1a2st.simplebackendpractice.wallet.controller;

import com.m1a2st.simplebackendpractice.wallet.dto.WalletRespDTO;
import com.m1a2st.simplebackendpractice.wallet.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("wallet")
    public WalletRespDTO showWallet(Principal principal){
        return walletService.getWallet(principal.getName());
    }
    

}
