package com.kiruthika.chatapp.messaging_service.service;

import com.kiruthika.chatapp.messaging_service.modal.MessageDelivery;
import com.kiruthika.chatapp.messaging_service.modal.MessageUserId;
import com.kiruthika.chatapp.messaging_service.repo.MessageDeliveryRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

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

    public Boolean isMessageDeliveredToUser(long msgId, long userId) {
        return messageDeliveryRepo.existsByMessageIdAndUserId(msgId,userId);
    }

    public Boolean isMessageDeliveredToAll(long msgId, Long groupMembersCount) {
        Long count=messageDeliveryRepo.countMessageDelivery(msgId);
        return Objects.equals(count, groupMembersCount);
    }
}
