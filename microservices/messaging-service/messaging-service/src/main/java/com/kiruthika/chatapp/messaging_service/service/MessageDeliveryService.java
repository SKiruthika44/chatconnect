package com.kiruthika.chatapp.messaging_service.service;

import com.kiruthika.chatapp.messaging_service.modal.MessageDelivery;
import com.kiruthika.chatapp.messaging_service.modal.MessageUserId;
import com.kiruthika.chatapp.messaging_service.repo.MessageDeliveryRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class MessageDeliveryService {

    private final MessageDeliveryRepo messageDeliveryRepo;
    public void addDelivery(Long senderId, long msgId) {
        MessageUserId messageUserId=new MessageUserId(msgId,senderId);
        MessageDelivery messageDelivery=new MessageDelivery();
        messageDelivery.setId(messageUserId);
        messageDelivery.setDeliveredAt(LocalDateTime.now());
        messageDeliveryRepo.save(messageDelivery);

    }
}
