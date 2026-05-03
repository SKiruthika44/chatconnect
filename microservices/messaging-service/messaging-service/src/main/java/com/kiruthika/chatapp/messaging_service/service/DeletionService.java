package com.kiruthika.chatapp.messaging_service.service;


import com.kiruthika.chatapp.messaging_service.MessageEventPublisher;
import com.kiruthika.chatapp.messaging_service.client.UserServiceClient;
import com.kiruthika.chatapp.messaging_service.event.MessageDeletedForMeEvent;
import com.kiruthika.chatapp.messaging_service.modal.MessageDeletion;
import com.kiruthika.chatapp.messaging_service.modal.MessageUserId;
import com.kiruthika.chatapp.messaging_service.repo.MessageDeletionRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class DeletionService {

    private final MessageDeletionRepo deletionRepo;

    private final MessageEventPublisher messageEventPublisher;

    private final UserServiceClient userServiceClient;

    public void deleteMessageForMe(long msgId, Long userId) {
        MessageUserId messageUserId=new MessageUserId();
        messageUserId.setMessageId(msgId);
        messageUserId.setUserId(userId);
        MessageDeletion messageDeletion=new MessageDeletion();
        messageDeletion.setId(messageUserId);
        messageDeletion.setDeletedAt(LocalDateTime.now());
        deletionRepo.save(messageDeletion);
        String username=userServiceClient.getUsernameById(userId);
        messageEventPublisher.publishDirectMessageDeletedForMe(new MessageDeletedForMeEvent(msgId,username));

    }


}
