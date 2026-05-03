package com.kiruthika.chatapp.messaging_service.service;


import com.kiruthika.chatapp.messaging_service.client.UserServiceClient;
import com.kiruthika.chatapp.messaging_service.client.WsServiceClient;
import com.kiruthika.chatapp.messaging_service.dto.GroupMessageRequestDto;
import com.kiruthika.chatapp.messaging_service.dto.GroupMessageResponseDto;
import com.kiruthika.chatapp.messaging_service.event.*;
import com.kiruthika.chatapp.messaging_service.modal.DirectMessage;
import com.kiruthika.chatapp.messaging_service.modal.GroupChat;
import com.kiruthika.chatapp.messaging_service.modal.GroupMessage;
import com.kiruthika.chatapp.messaging_service.publisher.GroupMessageEventPublisher;
import com.kiruthika.chatapp.messaging_service.repo.GroupMessageRepo;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class GroupMessageService {
    private final GroupMessageRepo groupMessageRepo;

    private final UserServiceClient userServiceClient;
    private final GroupService groupService;
    private final MessageDeliveryService messageDeliveryService;
    private final MessageReadService messageReadService;
    private GroupMemberService groupMemberService;
    private final WsServiceClient wsServiceClient;
    private final GroupMessageEventPublisher groupMessageEventPublisher;
    private final DeletionService deletionService;
    private final MessageReactionService messageReactionService;
    public void sendGroupMessage(String sender, GroupMessageRequestDto dto) {
        Long senderId = userServiceClient.getUserIdByUsername(sender);
        Long groupId = groupService.getGroupIdByGroupName(dto.getGroupName());
        GroupMessage groupMessage = new GroupMessage();
        groupMessage.setContent(dto.getContent());
        groupMessage.setSenderId(senderId);
        groupMessage.setGroupId(groupId);
        groupMessage.setDetectedLanguage("en");
        groupMessage.setCreatedAt(LocalDateTime.now());
        groupMessageRepo.save(groupMessage);
        GroupMessageResponseDto responseDto = createDto(groupMessage);
        //add sender,to delivery,read
        messageDeliveryService.addDelivery(senderId, groupMessage.getMsgId());
        messageReadService.addRead(senderId, groupMessage.getMsgId());
        //get groupmembers
        List<String> groupMembersName = groupMemberService.getGroupMembersByGroupId(groupMessage.getGroupId());

        Set<String> onlineUsers = wsServiceClient.getOnlineUsers();

        long delivered = 1;
        for (String member : groupMembersName) {
            if (onlineUsers.contains(member) && !member.equals(sender)) {
                System.out.println("online groupmember:" + member);
                Long memberId = userServiceClient.getUserIdByUsername(member);
                messageDeliveryService.addDelivery(memberId, groupMessage.getMsgId());
                delivered++;
            }
        }
        if (delivered == groupService.getGroupMembersCount(groupMessage.getGroupId())) {
            responseDto.setStatus("DELIVERED");
        }

        GroupMessageCreatedEvent groupMessageCreatedEvent = new GroupMessageCreatedEvent(responseDto, groupMessage.getGroupId());
        groupMessageEventPublisher.groupMessageCreated(groupMessageCreatedEvent);


    }

    private GroupMessageResponseDto createDto(GroupMessage groupMessage) {
        GroupMessageResponseDto dto = new GroupMessageResponseDto();
        dto.setId(groupMessage.getMsgId());
        dto.setCreatedAt(groupMessage.getCreatedAt());
        dto.setStatus("SENT");
        dto.setGroupName(groupService.getGroupNameById(groupMessage.getGroupId()));
        dto.setContent(groupMessage.getContent());
        dto.setSenderName(userServiceClient.getUsernameById(groupMessage.getSenderId()));
        if(groupMessage.isDeletedForEveryone()){
            dto.setDeletedForEveryone(true);
            dto.setContent(null);
        }
        Map<String,Long> emojis=messageReactionService.getGroupMessageEmojis(groupMessage.getMsgId());
        dto.setEmojisCount(emojis);
        return dto;

    }

    public ResponseEntity<List<GroupMessageResponseDto>> getMessagesForGroup(String username, String groupname) {
        List<GroupMessageResponseDto> responseDtos = new ArrayList<>();
        Long userId=userServiceClient.getUserIdByUsername(username);
        Long groupId = groupService.getGroupIdByGroupName(groupname);

        //List<GroupMessage> allMessages = groupMessageRepo.findByGroupId(groupId);
        List<GroupMessage> allMessages = groupMessageRepo.findAllVisibleGroupMessages(groupId,userId);
        for (GroupMessage groupMessage : allMessages) {
            GroupMessageResponseDto dto = createDto(groupMessage);
            dto=updateStatus(dto, groupMessage);
            responseDtos.add(dto);

        }
        return ResponseEntity.ok(responseDtos);


    }

    private GroupMessageResponseDto updateStatus(GroupMessageResponseDto dto, GroupMessage groupMessage) {
        Boolean isRead = messageReadService.isMessageReadByAll(groupMessage.getMsgId(), groupService.getGroupMembersCount(groupMessage.getGroupId()));
        if (isRead) {
            dto.setStatus("READ");
            return dto;
        }
        Boolean isDelivered = messageDeliveryService.isMessageDeliveredToAll(groupMessage.getMsgId(), groupService.getGroupMembersCount(groupMessage.getGroupId()));
        if (isDelivered) {
            dto.setStatus("DELIVERED");
        }
        return dto;
    }

    public void deliverDirectMessages(String username) {
        Long userId=userServiceClient.getUserIdByUsername(username);
        List<GroupChat> joinedGroups=groupService.getJoinedGroups(userId);
        for(GroupChat groupChat:joinedGroups){
            List<GroupMessage> groupMessages=groupMessageRepo.findByGroupId(groupChat.getId());
            for(GroupMessage groupMessage:groupMessages){
                Boolean isDelivered=messageDeliveryService.isMessageDeliveredToUser(groupMessage.getMsgId(),userId);
                if(!isDelivered){
                    messageDeliveryService.addDelivery(userId,groupMessage.getMsgId());


                    Boolean isDeliveredToAll=messageDeliveryService.isMessageDeliveredToAll(groupMessage.getMsgId(),groupChat.getGroupMembersCount());
                    if(isDeliveredToAll) {

                        GroupMessageResponseDto dto = createDto(groupMessage);
                        dto.setStatus("DELIVERED");
                        groupMessageEventPublisher.groupMessageStatusUpdated(new GroupMessageStatusUpdatedEvent(dto));
                    }
                }
            }
        }
    }

    public void markGroupMessageRead(String username, Long msgId) {
        Long userId=userServiceClient.getUserIdByUsername(username);
        messageReadService.addRead(userId,msgId);
        GroupMessage groupMessage=groupMessageRepo.findByMsgId(msgId);
        Long groupmembersCount=groupService.getGroupMembersCount(groupMessage.getGroupId());
        Boolean isRead=messageReadService.isMessageReadByAll(msgId,groupmembersCount);
        if(isRead){
            GroupMessageResponseDto dto = createDto(groupMessage);
            dto.setStatus("READ");
            groupMessageEventPublisher.groupMessageStatusUpdated(new GroupMessageStatusUpdatedEvent(dto));

        }

    }

    public void markAllMessagesRead(String username, String groupName) {
        Long userId=userServiceClient.getUserIdByUsername(username);
        Long groupId= groupService.getGroupIdByGroupName(groupName);
        List<GroupMessage> allMessages=groupMessageRepo.findByGroupId(groupId);
        Long groupMembersCount=groupService.getGroupMembersCount(groupId);
        for(GroupMessage groupMessage:allMessages){
            Boolean isRead=messageReadService.isMessageReadByUser(groupMessage.getMsgId(),userId);
            if(!isRead){
                System.out.println("content:"+groupMessage.getContent());
                messageReadService.addRead(userId,groupMessage.getMsgId());

                Boolean isReadByAll=messageReadService.isMessageReadByAll(groupMessage.getMsgId(),groupMembersCount);
                if(isReadByAll){
                    GroupMessageResponseDto dto = createDto(groupMessage);
                    dto.setStatus("READ");

                    groupMessageEventPublisher.groupMessageStatusUpdated(new GroupMessageStatusUpdatedEvent(dto));

                }
            }
        }

    }

    public ResponseEntity<Map<String, Integer>> getUnreadCountsForGroupChat(String username) {
        Map<String,Integer> unreadCounts=new HashMap<>();
        Long userId= userServiceClient.getUserIdByUsername(username);
        List<GroupChat> joinedGroups=groupService.getJoinedGroups(userId);
        for(GroupChat groupChat:joinedGroups){
            List<GroupMessage> allMessages=groupMessageRepo.findByGroupId(groupChat.getId());
            for(GroupMessage groupMessage:allMessages){
                Boolean isRead=messageReadService.isMessageReadByUser(groupMessage.getMsgId(),userId);
                if(!isRead){
                    unreadCounts.put(groupChat.getGroupName(),unreadCounts.getOrDefault(groupChat.getGroupName(),0)+1);

                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(unreadCounts);

    }

    public ResponseEntity<Void> editMessage(String username, long msgId, String content) {
        Long userId=userServiceClient.getUserIdByUsername(username);
        GroupMessage groupMessage=groupMessageRepo.findByMsgId(msgId);
        if(Objects.equals(groupMessage.getSenderId(), userId)){
            groupMessage.setContent(content);
            groupMessageRepo.save(groupMessage);
            GroupMessageResponseDto dto=createDto(groupMessage);
            dto=updateStatus(dto,groupMessage);
            groupMessageEventPublisher.groupMessageEdited(new GroupMessageEditedEvent(dto));
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public ResponseEntity<Void> deleteMessage(String username, long msgId, String scope) {
        Long userId=userServiceClient.getUserIdByUsername(username);
       GroupMessage groupMessage=groupMessageRepo.findByMsgId(msgId);
        if(scope.equals("me")){
            deletionService.deleteMessageForMe(msgId,userId);


        }
        else{
            groupMessage.setDeletedForEveryone(true);
            groupMessageRepo.save(groupMessage);
           GroupMessageResponseDto dto=createDto(groupMessage);
           dto=updateStatus(dto,groupMessage);

           groupMessageEventPublisher.groupMessageDeletedForEveryone(new GroupMessageDeletedForEveryoneEvent(dto));
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public ResponseEntity<Void> reactMessage(String username, long msgId, String emoji) {
        Long userId=userServiceClient.getUserIdByUsername(username);
        GroupMessage groupMessage=groupMessageRepo.findByMsgId(msgId);
        messageReactionService.reactMessage(groupMessage.getMsgId(),userId,emoji);
        GroupMessageResponseDto dto=createDto(groupMessage);
        dto=updateStatus(dto,groupMessage);
        groupMessageEventPublisher.groupMessageEmojiCreated(new GroupMessageEmojiCreatedEvent(dto));

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

