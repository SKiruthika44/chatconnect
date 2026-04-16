package com.kiruthika.chatapp.messaging_service.controller;


import com.kiruthika.chatapp.messaging_service.dto.GroupRequestDto;
import com.kiruthika.chatapp.messaging_service.dto.GroupResponseDto;
import com.kiruthika.chatapp.messaging_service.service.GroupService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/group")
public class GroupController {

    private final GroupService groupService;

    @PostMapping("/create-group")
    public ResponseEntity<GroupResponseDto> createGroup(@RequestHeader("X-User")String username, @RequestBody GroupRequestDto groupDTO){
        return groupService.createGroup(username,groupDTO);

    }

    @GetMapping("/all-groups/{username}")
    public ResponseEntity<List<GroupResponseDto>> sendUserGroups(@PathVariable String username){

        return groupService.sendUserGroups(username);
    }




}
