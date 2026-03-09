package com.chatConnect.backend.Config;

import com.chatConnect.backend.Event.MessageEditedEvent;
import com.chatConnect.backend.Modal.*;
import com.chatConnect.backend.Repo.MessageDeliveryRepo;
import com.chatConnect.backend.Repo.MessageReadRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component

public class EditListener {

    @Autowired

    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private MessageDeliveryRepo messageDeliveryRepo;

    @Autowired
    private MessageReadRepo messageReadRepo;

    @EventListener
    public void notifyPrivateMessageEdited(MessageEditedEvent messageEditedEvent){
       Message editedMessage=messageEditedEvent.getChatMessage();



        if(editedMessage instanceof GroupMessage){
            long groupId=((GroupMessage) editedMessage).getGroupChat().getId();
            GroupMessageEditedResponseDTO groupMessageEditedResponseDTO=new GroupMessageEditedResponseDTO(editedMessage.getMsg_id(), editedMessage.getContent());
            simpMessagingTemplate.convertAndSend("/topic/group-message/edit/"+groupId,groupMessageEditedResponseDTO);

        }
        else{



            Users receiver=((ChatMessage)editedMessage).getReceiver();
            ChatMessageResponseDTO chatMessageResponseDTO=new ChatMessageResponseDTO(editedMessage.getMsg_id(), editedMessage.getContent(),editedMessage.getSender().getUsername(),receiver.getUsername(),editedMessage.getCreatedAt(),"SENT");
            boolean delivered=messageDeliveryRepo.existsByMsgIdAndUserIdByCount(editedMessage.getMsg_id(), receiver.getId());
            if(delivered){
                chatMessageResponseDTO.setStatus("DELIVERED");
            }
            boolean read=messageReadRepo.existsByMsgIdAndUserIdByCount(editedMessage.getMsg_id(), receiver.getId());
            if(read){
                chatMessageResponseDTO.setStatus("READ");
            }
            if(editedMessage.isDeletedForEveryone()){
                chatMessageResponseDTO.setDeletedForEveryone(true);
            }
            simpMessagingTemplate.convertAndSendToUser(editedMessage.getSender().getUsername(),"/queue/message/edit",chatMessageResponseDTO);
            simpMessagingTemplate.convertAndSendToUser(receiver.getUsername(),"/queue/message/edit",chatMessageResponseDTO);

        }

    }
}
