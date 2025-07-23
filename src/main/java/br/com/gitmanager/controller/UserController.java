package br.com.gitmanager.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.gitmanager.dto.UserDTO;
import br.com.gitmanager.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsersWithRoles());
    }
    
    @PostMapping("/{userId}/roles")
    public ResponseEntity<UserDTO> assignRoleToUser(
            @PathVariable Long userId,
            @RequestParam String role) {
        return ResponseEntity.ok(userService.assignRoleToUser(userId, role));
    }
}
