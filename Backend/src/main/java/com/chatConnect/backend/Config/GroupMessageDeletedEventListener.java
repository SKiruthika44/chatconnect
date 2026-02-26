package com.chatConnect.backend.Config;

import com.chatConnect.backend.Event.GroupMessageDeletedEvent;
import com.chatConnect.backend.Modal.GroupMessage;
import com.chatConnect.backend.Modal.GroupMessageResponseDTO;
import com.chatConnect.backend.Modal.GroupResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class GroupMessageDeletedEventListener {
    @Autowired

    private SimpMessagingTemplate simpMessageTemplate;

    @EventListener

    public void notifyGroupMessageDeletedToMembers(GroupMessageDeletedEvent groupMessageDeletedEvent){
        GroupMessage groupMessage=groupMessageDeletedEvent.getGroupMessage();
        long groupId=groupMessage.getGroupChat().getId();
        GroupMessageResponseDTO groupMessageResponseDTO=new GroupMessageResponseDTO(groupMessage.getId(),null,groupMessage.getCreatedAt(),"SENT",groupMessage.getSender().getUsername(),groupMessage.getGroupChat().getGroupName());
        if(groupMessage.getDeliveredUsers().size()==groupMessage.getGroupChat().getGroupMembers().size()){
            groupMessageResponseDTO.setStatus("DELIVERED");
        }
        if(groupMessage.getReadUsers().size()==groupMessage.getGroupChat().getGroupMembers().size()){
            groupMessageResponseDTO.setStatus("READ");
        }
        groupMessageResponseDTO.setDeletedForEveryone(true);
        simpMessageTemplate.convertAndSend("/topic/group/delete/"+groupId,groupMessageResponseDTO);
        System.out.println("sent deletd event to path");


    }
}
