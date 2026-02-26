package com.chatConnect.backend.Repo;

import com.chatConnect.backend.Modal.MessageUserId;
import com.chatConnect.backend.Modal.PrivateMessageDeletedUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivateMessageDeletedUserRepo extends JpaRepository<PrivateMessageDeletedUser, MessageUserId> {
}
