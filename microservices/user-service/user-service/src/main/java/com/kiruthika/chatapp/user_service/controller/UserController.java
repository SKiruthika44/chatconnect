package com.kiruthika.chatapp.user_service.controller;


import com.kiruthika.chatapp.user_service.dto.LastSeenRequestDto;
import com.kiruthika.chatapp.user_service.dto.UserResponseDTO;
import com.kiruthika.chatapp.user_service.modal.User;
import com.kiruthika.chatapp.user_service.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.RsetController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {


    private final UserService userService;
    @GetMapping
    public ResponseEntity<UserResponseDTO> getLoggedInUser(@RequestHeader("X-User") String username){
        //String username=request.getHeader("X-User");
        System.out.println("request username:"+username);
        return userService.getLoggedInUser(username);
    }

    @GetMapping("/all-users")
    public List<UserResponseDTO> getAllUsers(@RequestHeader("X-User") String username){

        return userService.getAllUsers(username);

    }


    @GetMapping("/change-lang/{lang}")
    public ResponseEntity<UserResponseDTO> changeLanguage(@RequestHeader("X-User") String username,@PathVariable String lang){
        return userService.changeLanguage(username,lang);
    }

    @GetMapping("/search")
    public List<UserResponseDTO> searchUsers(@RequestParam(required=false)String keyword,@RequestHeader("X-User") String username){
        return userService.search(username,keyword);
    }

    @PutMapping("/lastseen")
    public ResponseEntity<Void> updateLastseen(@RequestBody LastSeenRequestDto dto){
        return userService.updateLastseen(dto);

    }

    @GetMapping("/id/{username}")
    public Long getId(@PathVariable String username){
        return userService.getId(username);

    }

    @GetMapping("/username/{id}")
    public String getUsername(@PathVariable Long id){
        return userService.getUsername(id);
    }



}
