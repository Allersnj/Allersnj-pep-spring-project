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

    /**
     * Uses the AccountRepository to check if an account exists based on its username
     * @param username The username of the account to check
     * @return Boolean indicating whether the account exists
     */
    public boolean accountExistsByUsername(String username)
    {
        return accountRepository.existsByUsername(username);
    }

    /**
     * Uses the AccountRepository to save an account if it meets these requirements:
     * * the username is not blank
     * * the password is at least 4 characters long
     * @param account The account to save
     * @return Optional<Account> of the account saved or empty if unsuccessful
     */
    public Optional<Account> saveAccount(Account account)
    {
        Optional<Account> result = Optional.empty();
        if (!account.getUsername().isBlank() && account.getPassword().length() >= 4)
        {
            result = Optional.of(accountRepository.save(account));
        }
        return result;
    }

    /**
     * Uses the AccountRepository to authenticate an account
     * @param account The account to authenticate
     * @return Optional<Account> of the authenticated account or empty if unsuccessful
     */
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
