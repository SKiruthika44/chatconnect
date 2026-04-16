package com.kiruthika.chatapp.messaging_service.service;


import com.kiruthika.chatapp.messaging_service.client.UserServiceClient;
import com.kiruthika.chatapp.messaging_service.client.WsServiceClient;
import com.kiruthika.chatapp.messaging_service.dto.GroupMessageRequestDto;
import com.kiruthika.chatapp.messaging_service.dto.GroupMessageResponseDto;
import com.kiruthika.chatapp.messaging_service.event.GroupMessageCreatedEvent;
import com.kiruthika.chatapp.messaging_service.event.GroupMessageStatusUpdatedEvent;
import com.kiruthika.chatapp.messaging_service.modal.GroupChat;
import com.kiruthika.chatapp.messaging_service.modal.GroupMessage;
import com.kiruthika.chatapp.messaging_service.publisher.GroupMessageEventPublisher;
import com.kiruthika.chatapp.messaging_service.repo.GroupMessageRepo;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        return dto;

    }

    public ResponseEntity<List<GroupMessageResponseDto>> getMessagesForGroup(String username, String groupname) {
        List<GroupMessageResponseDto> responseDtos = new ArrayList<>();
        Long groupId = groupService.getGroupIdByGroupName(groupname);
        List<GroupMessage> allMessages = groupMessageRepo.findByGroupId(groupId);
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
}

