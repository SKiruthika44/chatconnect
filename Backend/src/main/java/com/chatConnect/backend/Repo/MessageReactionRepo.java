package com.chatConnect.backend.Repo;

import com.chatConnect.backend.Modal.MessageReaction;
import com.chatConnect.backend.Modal.MessageUserId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageReactionRepo extends JpaRepository<MessageReaction, MessageUserId> {
}
