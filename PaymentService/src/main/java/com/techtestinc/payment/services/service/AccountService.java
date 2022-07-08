package com.techtestinc.payment.services.service;

import com.techtestinc.payment.services.entity.Account;

/**
 * @author yuvaraj.sanjeevi
 */
public interface AccountService {

    Account findByAccountId(Long accountId);
}
