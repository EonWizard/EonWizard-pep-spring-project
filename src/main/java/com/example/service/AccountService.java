package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    @Autowired
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    // create new account
    public Account newAccount(Account account){
        if(account.getUsername().isEmpty() || account.getPassword().length() < 4){
            throw new IllegalArgumentException("Invalid Credentials");
        }
        if(accountRepository.existsByUsername(account.getUsername())){
            throw new IllegalStateException("This username already exists"); 
        } 
        return accountRepository.save(account);

    }
    
    // verify login
    public Account loginAccount(String username, String password){
        Account account = accountRepository.findByUsername(username)
        .orElseThrow(() -> new IllegalArgumentException("Username not found"));
        if(!account.getPassword().equals(password)){
            throw new IllegalArgumentException("Incorrect Password");
        }

        return account;
    }



}
