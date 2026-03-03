package com.chatConnect.backend.Config;

import com.chatConnect.backend.Event.GroupMessageDeletedEvent;
import com.chatConnect.backend.Modal.GroupMessage;
import com.chatConnect.backend.Modal.GroupMessageResponseDTO;
import com.chatConnect.backend.Modal.GroupResponseDTO;
import com.chatConnect.backend.Repo.MessageDeliveryRepo;
import com.chatConnect.backend.Repo.MessageReadRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class GroupMessageDeletedEventListener {
    @Autowired

    private SimpMessagingTemplate simpMessageTemplate;

    @Autowired
    private MessageDeliveryRepo messageDeliveryRepo;

    @Autowired
    private MessageReadRepo messageReadRepo;

    @EventListener

    public void notifyGroupMessageDeletedToMembers(GroupMessageDeletedEvent groupMessageDeletedEvent){
        GroupMessage groupMessage=groupMessageDeletedEvent.getGroupMessage();
        long groupId=groupMessage.getGroupChat().getId();
        GroupMessageResponseDTO groupMessageResponseDTO=new GroupMessageResponseDTO(groupMessage.getMsg_id(),null,groupMessage.getCreatedAt(),"SENT",groupMessage.getSender().getUsername(),groupMessage.getGroupChat().getGroupName());

        long deliveredCount=messageDeliveryRepo.countByMsgIdAndUserId(groupMessage.getMsg_id());
        long readCount=messageReadRepo.countReadUsers(groupMessage.getMsg_id());
        if(deliveredCount==groupMessage.getGroupChat().getGroupMembers().size()){
            groupMessageResponseDTO.setStatus("DELIVERED");
        }
        if(readCount==groupMessage.getGroupChat().getGroupMembers().size()){
            groupMessageResponseDTO.setStatus("READ");
        }
        groupMessageResponseDTO.setDeletedForEveryone(true);
        simpMessageTemplate.convertAndSend("/topic/group/delete/"+groupId,groupMessageResponseDTO);



    }
}
