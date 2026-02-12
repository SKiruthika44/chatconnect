package com.chatConnect.backend.Controller;

import com.chatConnect.backend.Modal.UserPrincipal;
import com.chatConnect.backend.Modal.UserDTO;
import com.chatConnect.backend.Modal.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.chatConnect.backend.Service.UserService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@CrossOrigin

public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Users user){

        return service.register(user);
    }
    @PostMapping("/log-in")
    public ResponseEntity<String> login(@RequestBody Users user){
        return service.verify(user);

    }


    @GetMapping("/all-users")
    public List<UserDTO> getAllUsers(@AuthenticationPrincipal UserPrincipal userPrincipal){

        return service.getAllUsers(userPrincipal.getUsername());

    }

    @GetMapping("/change-lang/{lang}")
    public ResponseEntity<UserDTO> changeLanguage(@AuthenticationPrincipal UserPrincipal userPrincipal,@PathVariable String lang){
        return service.changeLanguage(userPrincipal.getUsername(),lang);
    }


    @PostMapping("/upload-image")
    public ResponseEntity<?> uploadImage(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam("image")MultipartFile file){
        return service.uploadImage(userPrincipal.getUsername(),file);
    }

    @GetMapping("/user")
    public ResponseEntity<Users> getLoggedInUser(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return service.getLoggedInUser(userPrincipal.getUsername());
    }
}
