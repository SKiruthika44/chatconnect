package com.chatConnect.backend.Repo;

import com.chatConnect.backend.Modal.MessageRead;
import com.chatConnect.backend.Modal.MessageUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository

public interface MessageReadRepo extends JpaRepository<MessageRead, MessageUserId> {
    @Query(value="select count(m)>0 from MessageRead m where m.id.messageId =:msgId and m.id.userId=:userId ")

    boolean existsByMsgIdAndUserIdByCount(@Param("msgId")Long msgId, @Param("userId")Long userId);

    @Query(value="select count(m) from MessageRead m where m.id.messageId =:msgId ")

    long countReadUsers(@Param("msgId")Long msgId);
}
