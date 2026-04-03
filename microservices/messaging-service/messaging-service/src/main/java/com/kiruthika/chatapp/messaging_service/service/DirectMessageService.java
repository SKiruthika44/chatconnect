package com.kiruthika.chatapp.messaging_service.service;

import com.kiruthika.chatapp.messaging_service.client.UserServiceClient;
import com.kiruthika.chatapp.messaging_service.client.WsServiceClient;
import com.kiruthika.chatapp.messaging_service.dto.PrivateMessageRequestDto;
import com.kiruthika.chatapp.messaging_service.modal.DirectMessage;
import com.kiruthika.chatapp.messaging_service.repo.DirectMessageRepo;
import lombok.AllArgsConstructor;
import org.bouncycastle.pqc.jcajce.provider.sphincsplus.SignatureSpi;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class DirectMessageService {

    private final WsServiceClient wsServiceClient;

    private final UserServiceClient userServiceClient;

    private final DirectMessageRepo directMessageRepo;

    private final MessageDeliveryService messageDeliveryService;
    public void sendDirectMessage(String sender, PrivateMessageRequestDto dto) {
        Long senderId=userServiceClient.getUserIdByUsername(sender);
        Long receiverId= userServiceClient.getUserIdByUsername(dto.getReceiver());
        Set<String> onlineUsers=wsServiceClient.getOnlineUsers();
        DirectMessage directMessage=new DirectMessage();
        directMessage.setSenderId(senderId);
        directMessage.setReceiverId(receiverId);
        directMessage.setContent(dto.getContent());
        directMessage.setDetectedLanguage("en");
        directMessageRepo.save(directMessage);
        messageDeliveryService.addDelivery(directMessage.getSenderId(),directMessage.getMsgId());
        if(onlineUsers.contains(dto.getReceiver())){
            messageDeliveryService.addDelivery(directMessage.getReceiverId(),directMessage.getMsgId());
        }


    }
}
