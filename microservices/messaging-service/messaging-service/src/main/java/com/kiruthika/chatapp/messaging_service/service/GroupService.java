package com.kiruthika.chatapp.messaging_service.service;

import com.kiruthika.chatapp.messaging_service.client.UserServiceClient;
import com.kiruthika.chatapp.messaging_service.dto.GroupRequestDto;
import com.kiruthika.chatapp.messaging_service.dto.GroupResponseDto;
import com.kiruthika.chatapp.messaging_service.event.GroupCreatedEvent;
import com.kiruthika.chatapp.messaging_service.modal.GroupChat;
import com.kiruthika.chatapp.messaging_service.modal.GroupMember;
import com.kiruthika.chatapp.messaging_service.publisher.GroupEventPublisher;
import com.kiruthika.chatapp.messaging_service.repo.GroupChatRepo;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class GroupService {

    private final UserServiceClient userServiceClient;

    private final GroupChatRepo groupChatRepo;
    private final GroupMemberService groupMemberService;
    private final GroupEventPublisher groupEventPublisher;
    public ResponseEntity<GroupResponseDto> createGroup(String username, GroupRequestDto groupDTO) {
        Long adminId= userServiceClient.getUserIdByUsername(username);
        GroupChat groupChat=new GroupChat();
        groupChat.setGroupName(groupDTO.getGroupName());
        groupChat.setCreatedAt(LocalDateTime.now());
        groupChat.setAdminId(adminId);
        groupChatRepo.save(groupChat);
        List<String> groupMembers=new ArrayList<>();
        long groupMembersCount=1;
        //add groupMembers
        for(String groupMemberName:groupDTO.getGroupMembers()){
            Long groupMemberId= userServiceClient.getUserIdByUsername(groupMemberName);
            groupMembers.add(groupMemberName);
            groupMemberService.addGroupMember(groupChat.getId(),groupMemberId);
            groupMembersCount++;
        }
        groupChat.setGroupMembersCount(groupMembersCount);
        groupChatRepo.save(groupChat);
        String adminName=userServiceClient.getUsernameById(groupChat.getAdminId());
        groupMemberService.addGroupMember(groupChat.getId(),adminId); //add admin to groupmembers
        groupMembers.add(adminName);
        GroupResponseDto groupResponseDto=new GroupResponseDto();
        groupResponseDto.setId(groupChat.getId());
        groupResponseDto.setGroupName(groupChat.getGroupName());
        groupResponseDto.setCreatedAt(groupChat.getCreatedAt());
        groupResponseDto.setAdmin(adminName);
        groupResponseDto.setGroupMembers(groupMembers);
        groupEventPublisher.publishGroupCreatedEvent(new GroupCreatedEvent(groupResponseDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(groupResponseDto);

    }

    public Long getGroupIdByGroupName(String groupName) {
        GroupChat groupChat=groupChatRepo.findByGroupName(groupName);
        return groupChat.getId();
    }

    public String getGroupNameById(Long groupId) {
        GroupChat groupChat=groupChatRepo.findByGroupId(groupId);
        return groupChat.getGroupName();
    }
    public Long getGroupMembersCount(Long groupId){
        GroupChat groupChat=groupChatRepo.findByGroupId(groupId);
        return groupChat.getGroupMembersCount();
    }

    public ResponseEntity<List<GroupResponseDto>> sendUserGroups(String username) {
        Long userId=userServiceClient.getUserIdByUsername(username);
        List<GroupChat> joinedGroups=getJoinedGroups(userId);

        List<GroupResponseDto> responseDtos=new ArrayList<>();
        for(GroupChat groupChat:joinedGroups){
            GroupResponseDto dto=createGroupResponseDto(groupChat);
            responseDtos.add(dto);
        }
        return ResponseEntity.ok(responseDtos);


    }

    private GroupResponseDto createGroupResponseDto(GroupChat groupChat) {
        GroupResponseDto dto=new GroupResponseDto();
        dto.setGroupName(groupChat.getGroupName());
        dto.setCreatedAt(groupChat.getCreatedAt());
        dto.setId(groupChat.getId());
        dto.setAdmin(userServiceClient.getUsernameById(groupChat.getAdminId()));
        List<String> groupmembersname=groupMemberService.getGroupMembersByGroupId(groupChat.getId());
        dto.setGroupMembers(groupmembersname);
        return dto;


    }



    public List<GroupChat> getJoinedGroups(Long userId) {
        List<GroupMember> groupMembers=groupMemberService.getGroupMemberByUserId(userId);
        List<GroupChat> joinedGroups=new ArrayList<>();
        for(GroupMember groupMember:groupMembers){
            joinedGroups.add(groupChatRepo.findByGroupId(groupMember.getId().getGroupId()));

        }
        return joinedGroups;

    }
}
