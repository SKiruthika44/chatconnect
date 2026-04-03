package com.chatConnect.backend.Repo;

import com.chatConnect.backend.Modal.MessageDeletion;
import com.chatConnect.backend.Modal.MessageUserId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageDeletionRepo extends JpaRepository<MessageDeletion, MessageUserId> {

}
