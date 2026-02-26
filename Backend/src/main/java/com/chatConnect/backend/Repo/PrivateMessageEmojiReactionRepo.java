package com.chatConnect.backend.Repo;

import com.chatConnect.backend.Modal.MessageUserId;
import com.chatConnect.backend.Modal.PrivateMessageEmojiReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface PrivateMessageEmojiReactionRepo extends JpaRepository<PrivateMessageEmojiReaction, MessageUserId> {

}
