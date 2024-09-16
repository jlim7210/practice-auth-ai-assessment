package com.example.practiceauth2.service.impl;

import com.example.practiceauth2.exception.EmailAlreadyInUseException;
import com.example.practiceauth2.model.dto.SignUpRequestDto;
import com.example.practiceauth2.model.entity.Account;
import com.example.practiceauth2.service.AccountService;
import com.example.practiceauth2.service.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void signUp(SignUpRequestDto signUpRequest) {
        if (this.existsByEmail(signUpRequest.getEmail())) {
            throw new EmailAlreadyInUseException();
        }

        final String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());
        final Account account = new Account(signUpRequest.getEmail(), signUpRequest.getName(), encodedPassword, Set.of("ROLE_USER"));
        accountRepository.save(account);
    }

    @Override
    public boolean existsByEmail(String email) {
        return accountRepository.findByEmail(email).isPresent();
    }

    @Override
    public void save(Account account) {
        accountRepository.save(account);
    }
}
