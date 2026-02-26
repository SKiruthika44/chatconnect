package com.chatConnect.backend.Controller;


import com.chatConnect.backend.Modal.*;

import com.chatConnect.backend.Service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController

public class GroupController {

    @Autowired

    GroupService groupService;

    @PostMapping("/group/create-group")

    public ResponseEntity<GroupResponseDTO> createGroup(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody GroupDTO groupDTO){

        return groupService.createGroup(userPrincipal.getUsername(),groupDTO);


    }

    @GetMapping("/group/unread")
    public Map<String,Integer> unreadMessagesPerGroup(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return groupService.getUnReadMessagesPerGroup(userPrincipal.getUsername());
    }

    @GetMapping("/group/messages/{groupname}")
    public List<GroupMessageResponseDTO> getMessagesForGroup(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable String groupname){
        return groupService.getMessagesForGroup(userPrincipal.getUsername(), groupname);
    }

    @PutMapping("/group/makeRead/{groupname}")
    public void markMessagesRead(@AuthenticationPrincipal UserPrincipal userPrincipal,@PathVariable String groupname){
        groupService.markMessagesRead(userPrincipal.getUsername(),groupname);
    }

    @PutMapping("/group/updateGroupMessageRead/{msgId}")
    public void markGroupMessageRead(@AuthenticationPrincipal UserPrincipal userPrincipal,@PathVariable int msgId){
        groupService.markGroupMessageRead(userPrincipal.getUsername(),msgId);

    }

    @MessageMapping("/group")
    public void sendGroupMessage(@AuthenticationPrincipal UserPrincipal userPrincipal,@RequestBody GroupMessageRequestDTO groupMessage){
        groupService.sendGroupMessage(userPrincipal.getUsername(),groupMessage);
    }

}
