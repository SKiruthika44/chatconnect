package com.chatConnect.backend.Config;

import com.chatConnect.backend.Event.MessageDeletedForEveryoneEvent;
import com.chatConnect.backend.Modal.*;
import com.chatConnect.backend.Repo.MessageDeliveryRepo;
import com.chatConnect.backend.Repo.MessageReadRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageDeletedForEveryoneEventListener {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private MessageDeliveryRepo messageDeliveryRepo;

    @Autowired
    private MessageReadRepo messageReadRepo;

    @EventListener

    public void handleMessageDeleted(MessageDeletedForEveryoneEvent messageDeletedForEveryoneEvent){
        Message message=messageDeletedForEveryoneEvent.getDeletedMsg();
        if(message instanceof ChatMessage){
            ChatMessage chatMessage=(ChatMessage)message;
            ChatMessageResponseDTO chatMessageResponseDTO=new ChatMessageResponseDTO(chatMessage.getMsg_id(), null,chatMessage.getSender().getUsername(),chatMessage.getReceiver().getUsername(),chatMessage.getCreatedAt(),"SENT");

            Users receiver=chatMessage.getReceiver();
            boolean delivered=messageDeliveryRepo.existsByMsgIdAndUserIdByCount(chatMessage.getMsg_id(), receiver.getId());
            if(delivered){
                chatMessageResponseDTO.setStatus("DELIVERED");
            }
            boolean read=messageReadRepo.existsByMsgIdAndUserIdByCount(chatMessage.getMsg_id(), receiver.getId());
            if(read){
                chatMessageResponseDTO.setStatus("READ");
            }
            chatMessageResponseDTO.setDeletedForEveryone(true);
            simpMessagingTemplate.convertAndSendToUser(chatMessage.getReceiver().getUsername(),"/queue/delete",chatMessageResponseDTO);
            simpMessagingTemplate.convertAndSendToUser(chatMessage.getSender().getUsername(),"/queue/delete",chatMessageResponseDTO);
        }
        else{
            GroupMessage groupMessage=(GroupMessage) message;
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
            simpMessagingTemplate.convertAndSend("/topic/group/delete/"+groupId,groupMessageResponseDTO);
        }
    }


}
