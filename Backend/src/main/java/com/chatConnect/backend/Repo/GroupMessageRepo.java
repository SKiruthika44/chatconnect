package com.chatConnect.backend.Repo;

import com.chatConnect.backend.Modal.GroupChat;
import com.chatConnect.backend.Modal.GroupMessage;
import com.chatConnect.backend.Modal.Users;
//import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
//import org.springframework.transaction.annotation.Propagation;

import java.util.List;


@Repository
public interface GroupMessageRepo extends JpaRepository<GroupMessage,Long> {


    List<GroupMessage> findByGroupChatAndDeliveredUsersNotContaining(GroupChat groupChat, Users receiver);

    List<GroupMessage> findByGroupChatAndReadUsersNotContaining(GroupChat group, Users user);

    List<GroupMessage> findByGroupChat(GroupChat group);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Modifying
    @Query(value="Insert Ignore into `read-users` (`groupmessage-id`,`user-id`) values(:msgId,:userId)",nativeQuery = true)

    void addReadUser(@Param("msgId")Long msgId, @Param("userId")Long userId);
}
