package com.kiruthika.chatapp.user_service.controller;

import com.kiruthika.chatapp.user_service.dto.UserRequestDTO;
import com.kiruthika.chatapp.user_service.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")

    public ResponseEntity<String> registerUser(@RequestBody UserRequestDTO userRequestDTO){
            return userService.registerUser(userRequestDTO);
    }

    @PostMapping("/login")

    public ResponseEntity<String> loginUser(@RequestBody UserRequestDTO userRequestDTO){
        return userService.loginUser(userRequestDTO);
    }



}
