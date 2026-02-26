package com.chatConnect.backend.Repo;

import com.chatConnect.backend.Modal.GroupMessageDeletedUser;
import com.chatConnect.backend.Modal.MessageUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupMessageDeletedUserRepo extends JpaRepository<GroupMessageDeletedUser, MessageUserId> {



}
