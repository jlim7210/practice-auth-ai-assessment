package com.example.practiceauth2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class SignUpController {

    @GetMapping("/signup")
    public String getSignUpPage() {
        return "signup";
    }
}
