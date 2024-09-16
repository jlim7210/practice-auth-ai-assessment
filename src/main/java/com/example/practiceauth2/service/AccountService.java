package com.example.practiceauth2.service;

import com.example.practiceauth2.model.dto.SignUpRequestDto;
import com.example.practiceauth2.model.entity.Account;

public interface AccountService {
    void signUp(SignUpRequestDto signUpRequest);
    boolean existsByEmail(String email);
    void save(Account account);
}
