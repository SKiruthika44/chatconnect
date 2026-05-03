package com.kiruthika.chatapp.messaging_service.repo;

import com.kiruthika.chatapp.messaging_service.modal.MessageDeletion;
import com.kiruthika.chatapp.messaging_service.modal.MessageUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface MessageDeletionRepo extends JpaRepository<MessageDeletion, MessageUserId> {
    
}
