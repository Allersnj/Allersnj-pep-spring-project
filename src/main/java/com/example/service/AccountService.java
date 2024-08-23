package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService
{
    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository)
    {
        this.accountRepository = accountRepository;
    }

    public boolean accountExistsByUsername(String username)
    {
        return accountRepository.existsByUsername(username);
    }

    public Optional<Account> saveAccount(Account account)
    {
        Optional<Account> result = Optional.empty();
        if (!account.getUsername().isBlank() && account.getPassword().length() >= 4)
        {
            result = Optional.of(accountRepository.save(account));
        }
        return result;
    }

    public Optional<Account> getAuthenticatedAccount(Account account)
    {
        Optional<Account> authenticatedAccount = Optional.empty();
        Optional<Account> matchedUsername = accountRepository.findByUsername(account.getUsername());

        if (matchedUsername.isPresent() && matchedUsername.get().getPassword().equals(account.getPassword()))
        {
            authenticatedAccount = Optional.of(matchedUsername.get());
        }

        return authenticatedAccount;
    }
}
