package dev.com.quiz.controller;

import dev.com.quiz.DTO.JwtResponse;
import dev.com.quiz.DTO.LoginRequest;
import dev.com.quiz.Utils.JwtUtil;
import dev.com.quiz.Utils.PasswordUtil;
import dev.com.quiz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {


    private final UserService userService ;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        // Example password check

        if (!PasswordUtil.verifyPassword(
                request.getPassword(),
                userService.getPasswordFromDB(request.getUsername()))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = JwtUtil.generateToken(request.getUsername());

        return ResponseEntity.ok(new JwtResponse(token));
    }
}

