package com.example.practiceauth2.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
//    @PreAuthorize("hasAuthority('ADMIN')") // hasAuthority는 권한을 검사하고 hasRole은 권한 이름을 검사한다. hasRole은 prefix character: 'ROLE_'을 붙여서 사용해야 한다.
    public String adminAccess() {
        return "Admin content";
    }

}
