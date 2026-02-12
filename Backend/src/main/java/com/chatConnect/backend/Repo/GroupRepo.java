package com.chatConnect.backend.Repo;


import com.chatConnect.backend.Modal.GroupChat;
import com.chatConnect.backend.Modal.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface GroupRepo extends JpaRepository<GroupChat,Long> {



    List<GroupChat> findByGroupMembersContaining(Users user);

    GroupChat findByGroupName(String groupname);

    List<GroupChat> findByGroupMembersContainingAndGroupNameContainingIgnoreCase(Users user, String keyword);
}
