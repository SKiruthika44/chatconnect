package com.kiruthika.chatapp.messaging_service.service;

import com.kiruthika.chatapp.messaging_service.client.UserServiceClient;
import com.kiruthika.chatapp.messaging_service.modal.GroupMember;
import com.kiruthika.chatapp.messaging_service.modal.GroupUserId;
import com.kiruthika.chatapp.messaging_service.repo.GroupMemberRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class GroupMemberService {
    private final GroupMemberRepo groupMemberRepo;
    private final UserServiceClient userServiceClient;

    public void addGroupMember(Long groupId, Long groupMemberId) {
        GroupUserId groupUserId=new GroupUserId(groupId,groupMemberId);

        GroupMember groupMember=new GroupMember();
        groupMember.setId(groupUserId);
        groupMember.setJoinedAt(LocalDateTime.now());
        groupMemberRepo.save(groupMember);
    }

    public List<String> getGroupMembersByGroupId(Long groupId) {
        List<String> groupMembersName=new ArrayList<>();
        List<GroupMember> groupMembers=groupMemberRepo.findByGroupId(groupId);

        for(GroupMember groupMember:groupMembers){
            Long userId=groupMember.getId().getUserId();
            String name= userServiceClient.getUsernameById(userId);
            groupMembersName.add(name);

        }
        return groupMembersName;

    }

    public List<GroupMember> getGroupMemberByUserId(Long userId) {
        return groupMemberRepo.findByUserId(userId);
    }
}
