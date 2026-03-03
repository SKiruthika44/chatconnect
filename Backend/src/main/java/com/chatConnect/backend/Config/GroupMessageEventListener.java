package com.chatConnect.backend.Config;

import com.chatConnect.backend.Event.GroupMessageCreatedEvent;
import com.chatConnect.backend.Modal.GroupMessage;
import com.chatConnect.backend.Modal.GroupMessageResponseDTO;
import com.chatConnect.backend.Repo.MessageDeliveryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component

public class GroupMessageEventListener {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private MessageDeliveryRepo messageDeliveryRepo;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendMessageAfterCommit(GroupMessageCreatedEvent groupMessageCreatedEvent){
        GroupMessage groupMessage=groupMessageCreatedEvent.getGroupMessage();
        GroupMessageResponseDTO responseDTO=new GroupMessageResponseDTO(groupMessage.getMsg_id(), groupMessage.getContent(), groupMessage.getCreatedAt(),"SENT",groupMessage.getSender().getUsername(),groupMessage.getGroupChat().getGroupName());
        if(messageDeliveryRepo.countByMsgIdAndUserId(groupMessage.getMsg_id())==groupMessage.getGroupChat().getGroupMembers().size()){
            responseDTO.setStatus("DELIVERED");
        }

        simpMessagingTemplate.convertAndSend("/topic/group/"+groupMessage.getGroupChat().getId(),responseDTO);
        System.out.println("message sent after commit"+groupMessage.getMsg_id());

    }
}
