package com.chatConnect.backend.Repo;

import com.chatConnect.backend.Modal.GroupMessageEmojiReaction;
import com.chatConnect.backend.Modal.MessageUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface GroupMessageEmojiReactionRepo extends JpaRepository<GroupMessageEmojiReaction, MessageUserId> {

}
