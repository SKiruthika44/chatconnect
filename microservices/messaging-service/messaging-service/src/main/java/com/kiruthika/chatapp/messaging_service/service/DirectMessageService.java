package com.kiruthika.chatapp.messaging_service.service;

import com.kiruthika.chatapp.messaging_service.MessageEventPublisher;
import com.kiruthika.chatapp.messaging_service.client.UserServiceClient;
import com.kiruthika.chatapp.messaging_service.client.WsServiceClient;
import com.kiruthika.chatapp.messaging_service.dto.DirectMessageResponseDto;
import com.kiruthika.chatapp.messaging_service.dto.PrivateMessageRequestDto;
import com.kiruthika.chatapp.messaging_service.event.DirectMessageCreatedEvent;
import com.kiruthika.chatapp.messaging_service.event.DirectMessageStatusUpdatedEvent;
import com.kiruthika.chatapp.messaging_service.modal.DirectMessage;
import com.kiruthika.chatapp.messaging_service.repo.DirectMessageRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.bouncycastle.pqc.jcajce.provider.sphincsplus.SignatureSpi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class DirectMessageService {

    private final WsServiceClient wsServiceClient;

    private final UserServiceClient userServiceClient;

    private final DirectMessageRepo directMessageRepo;

    private final MessageDeliveryService messageDeliveryService;

    private final MessageReadService messageReadService;

    private final MessageEventPublisher messageEventPublisher;

    @Transactional
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
        DirectMessageResponseDto responseDto=new DirectMessageResponseDto();
        String status="SENT";
        if(onlineUsers.contains(dto.getReceiver())){
            messageDeliveryService.addDelivery(directMessage.getReceiverId(),directMessage.getMsgId());
            status="DELIVERED";
        }
        messageReadService.addRead(senderId,directMessage.getMsgId());

        responseDto.setId(directMessage.getMsgId());
        responseDto.setContent(directMessage.getContent());
        responseDto.setStatus(status);
        responseDto.setReceiverName(dto.getReceiver());
        responseDto.setSenderName(sender);
        responseDto.setCreatedAt(directMessage.getCreatedAt());
        DirectMessageCreatedEvent event=new DirectMessageCreatedEvent(responseDto);
        messageEventPublisher.publishDirectMessageCreated(event);





    }



    @Transactional
    public ResponseEntity<Void> markMessageRead(String username, Long msgId) {
            Long userId=userServiceClient.getUserIdByUsername(username);
            DirectMessage directMessage=directMessageRepo.findById(msgId).orElseThrow(()->new RuntimeException("Message no found"));
            if(!directMessage.getSenderId().equals(userId)){
                messageReadService.addRead(userId,msgId);
                DirectMessageResponseDto responseDto=new DirectMessageResponseDto();
                responseDto.setId(directMessage.getMsgId());
                responseDto.setContent(directMessage.getContent());
                responseDto.setStatus("READ");
                String senderName=userServiceClient.getUsernameById(directMessage.getSenderId());
                responseDto.setReceiverName(username);
                responseDto.setSenderName(senderName);
                responseDto.setCreatedAt(directMessage.getCreatedAt());

                messageEventPublisher.publishDirectMessageStatusUpdated(new DirectMessageStatusUpdatedEvent(responseDto));
                return ResponseEntity.status(HttpStatus.OK).build();
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

    }

    public ResponseEntity<Void> markMessagesRead(String receiver, String sender) {
        Long receiverId=userServiceClient.getUserIdByUsername(receiver);
        Long senderId=userServiceClient.getUserIdByUsername(sender);
        List<DirectMessage> allMessages=directMessageRepo.findByReceiverIdAndSenderId(receiverId,senderId);
        for(DirectMessage directMessage:allMessages){
            Boolean exists=messageReadService.isMessageReadByUser(directMessage.getMsgId(),receiverId);
            if(!exists){
                messageReadService.addRead(receiverId, directMessage.getMsgId());
                DirectMessageResponseDto responseDto=new DirectMessageResponseDto();
                responseDto.setId(directMessage.getMsgId());
                responseDto.setContent(directMessage.getContent());
                responseDto.setStatus("READ");
                String senderName=userServiceClient.getUsernameById(directMessage.getSenderId());
                String receiverName=userServiceClient.getUsernameById(directMessage.getReceiverId());
                responseDto.setReceiverName(receiverName);
                responseDto.setSenderName(senderName);
                responseDto.setCreatedAt(directMessage.getCreatedAt());

                messageEventPublisher.publishDirectMessageStatusUpdated(new DirectMessageStatusUpdatedEvent(responseDto));


            }

        }
        return ResponseEntity.status(HttpStatus.OK).build();


    }

    public ResponseEntity<Map<String, Integer>> getUnReadCounts(String username) {
        Long userId= userServiceClient.getUserIdByUsername(username);
        Map<String,Integer> unReadCounts=new HashMap<>();
        List<DirectMessage> allMessages=directMessageRepo.findByReceiverId(userId);
        for(DirectMessage directMessage:allMessages){
            Boolean exists=messageReadService.isMessageReadByUser(directMessage.getMsgId(),userId);
            if(!exists){
                String senderName=userServiceClient.getUsernameById(directMessage.getSenderId());
                unReadCounts.put(senderName, unReadCounts.getOrDefault(senderName,0)+1);
            }
        }
        System.out.println("unReadCounts:"+unReadCounts);
        return ResponseEntity.status(HttpStatus.OK).body(unReadCounts);

    }

    public ResponseEntity<List<DirectMessageResponseDto>> getAllMessagesBetweenSenderAndReceiver(String username, String otherUsername) {
        List<DirectMessageResponseDto> responseDtos=new ArrayList<>();
        Long userId=userServiceClient.getUserIdByUsername(username);
        Long otherUserId=userServiceClient.getUserIdByUsername(otherUsername);
        List<DirectMessage> allMessages=directMessageRepo.findAllMessagesBetweenSenderAndReceiver(userId,otherUserId);
        for(DirectMessage directMessage:allMessages){
            DirectMessageResponseDto dto=new DirectMessageResponseDto();
            dto.setContent(directMessage.getContent());
            dto.setId(directMessage.getMsgId());
            String receiverName=userServiceClient.getUsernameById(directMessage.getReceiverId());
            String senderName=userServiceClient.getUsernameById(directMessage.getSenderId());
            dto.setSenderName(senderName);
            dto.setReceiverName(receiverName);
            dto.setCreatedAt(directMessage.getCreatedAt());
            dto.setStatus("SENT");
            Boolean isDelivered=messageDeliveryService.isMessageDeliveredToUser(directMessage.getMsgId(),directMessage.getMsgId());
            if(isDelivered){
                dto.setStatus("DELIVERED");
            }
            Boolean isRead=messageReadService.isMessageReadByUser(directMessage.getMsgId(), directMessage.getReceiverId());
            if(isRead){
                dto.setStatus("READ");
            }
            responseDtos.add(dto);

        }
        return ResponseEntity.ok(responseDtos);

    }

    public void deliverDirectMessages(String username) {
        Long receiverId= userServiceClient.getUserIdByUsername(username);
        List<DirectMessage> allMessages=directMessageRepo.findByReceiverId(receiverId);
        for(DirectMessage directMessage:allMessages){
            Boolean exists=messageDeliveryService.isMessageDeliveredToUser(directMessage.getMsgId(),receiverId);
            if(!exists){
                messageDeliveryService.addDelivery(receiverId,directMessage.getMsgId());
                DirectMessageResponseDto dto=new DirectMessageResponseDto();
                dto.setContent(directMessage.getContent());
                dto.setId(directMessage.getMsgId());
                String receiverName=userServiceClient.getUsernameById(directMessage.getReceiverId());
                String senderName=userServiceClient.getUsernameById(directMessage.getSenderId());
                dto.setSenderName(senderName);
                dto.setReceiverName(receiverName);
                dto.setCreatedAt(directMessage.getCreatedAt());
                dto.setStatus("DELIVERED");
                messageEventPublisher.publishDirectMessageStatusUpdated(new DirectMessageStatusUpdatedEvent(dto));

            }
        }
    }
}
