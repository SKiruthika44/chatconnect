package com.kiruthika.chatapp.messaging_service.controller;


import com.kiruthika.chatapp.messaging_service.dto.GroupMessageRequestDto;
import com.kiruthika.chatapp.messaging_service.dto.GroupMessageResponseDto;
import com.kiruthika.chatapp.messaging_service.dto.PrivateMessageRequestDto;
import com.kiruthika.chatapp.messaging_service.service.GroupMessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/message/group")
public class GroupMessageController {
    private final GroupMessageService groupMessageService;

    @PostMapping("/{sender}")
    public void sendGroupMessage(@PathVariable String sender, @RequestBody GroupMessageRequestDto dto){

        groupMessageService.sendGroupMessage(sender,dto);
    }

    @GetMapping("/{groupname}")
    public ResponseEntity<List<GroupMessageResponseDto>> getMessagesForGroup(@RequestHeader("X-User") String username, @PathVariable String groupname){
        return groupMessageService.getMessagesForGroup(username, groupname);
    }

    @PutMapping("/deliver/{username}")
    public void deliverGroupMessages(@PathVariable String username){
        groupMessageService.deliverDirectMessages(username);
    }

    @PutMapping("/mark-read/{msgId}")
    public void mrkGroupMessageRead(@RequestHeader("X-User") String username,@PathVariable Long msgId){
        groupMessageService.markGroupMessageRead(username,msgId);
    }

    @PutMapping("/mark-all-read/{groupname}")
    public void markAllMessagesRead(@RequestHeader("X-User")String username,@PathVariable String groupname){
        groupMessageService.markAllMessagesRead(username,groupname);
    }

    @GetMapping("/unread")
    public ResponseEntity<Map<String, Integer>> unreadCount(@RequestHeader("X-User")String username){
        return groupMessageService.getUnreadCountsForGroupChat(username);
    }

}
