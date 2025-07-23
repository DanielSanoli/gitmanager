package br.com.gitmanager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuth2SuccessController {
    @GetMapping("/api/auth/success")
    public Object success(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            System.out.println(principal);
            return ResponseEntity.status(401).body("Usuário não autenticado via OAuth2");
        }
        return principal.getAttributes();
    }
}
