package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account persistAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account getAccountById(Integer id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);

        if (optionalAccount.isPresent()) {
            return optionalAccount.get();
        }
        return null;
    }

    public Account getAccountByUsername(String username) {
        Optional<Account> optionalAccount = accountRepository.findByUsername(username);
        return optionalAccount.isPresent() ? optionalAccount.get() : null;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }


    public Account verifyAccount(String username, String password) {
        Optional<Account> optionalAccount = accountRepository.findByUsernameAndPassword(username, password);
        return optionalAccount.isPresent() ? optionalAccount.get() : null;
    }
    
    public Account updateAccount(Integer id, Account updatedAccount) {
        Optional<Account> optionalAccount = accountRepository.findById(id);

        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            account.setUsername(updatedAccount.getUsername());
            account.setPassword(updatedAccount.getPassword());
            accountRepository.save(account);
        }
        return null;
    }

    public void deleteAccountById(Integer id) {
        accountRepository.deleteById(id);
    }
}
