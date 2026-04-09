package com.kiruthika.chatapp.messaging_service.service;

import com.kiruthika.chatapp.messaging_service.modal.MessageDelivery;
import com.kiruthika.chatapp.messaging_service.modal.MessageRead;
import com.kiruthika.chatapp.messaging_service.modal.MessageUserId;
import com.kiruthika.chatapp.messaging_service.repo.MessageReadRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class MessageReadService {
    private final MessageReadRepo messageReadRepo;

    public void addRead(Long receiverId, long msgId) {
        MessageUserId messageUserId=new MessageUserId(msgId,receiverId);
        MessageRead messageRead=new MessageRead();
        messageRead.setId(messageUserId);
        messageRead.setReadAt(LocalDateTime.now());
        messageReadRepo.save(messageRead);

    }

    public Boolean isMessageReadByUser(long msgId, Long receiverId) {
        return messageReadRepo.existsByMessageIdAndUserId(msgId,receiverId);
    }
}
