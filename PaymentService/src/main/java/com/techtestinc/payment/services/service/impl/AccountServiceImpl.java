package com.techtestinc.payment.services.service.impl;

import com.techtestinc.payment.services.constants.AppConstants;
import com.techtestinc.payment.services.entity.Account;
import com.techtestinc.payment.services.exception.ResourceNotFoundException;
import com.techtestinc.payment.services.repository.AccountRepository;
import com.techtestinc.payment.services.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author yuvaraj.sanjeevi
 */
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public Account findByAccountId(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.ACCOUNT_STR, AppConstants.ACCOUNT_ID, accountId));
    }
}
