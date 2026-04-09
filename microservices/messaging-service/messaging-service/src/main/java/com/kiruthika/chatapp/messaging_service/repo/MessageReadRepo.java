package com.kiruthika.chatapp.messaging_service.repo;

import com.kiruthika.chatapp.messaging_service.modal.MessageRead;
import com.kiruthika.chatapp.messaging_service.modal.MessageUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageReadRepo extends JpaRepository<MessageRead, MessageUserId> {
    @Query(value="select count(m) >0 from MessageRead m where m.id.messageId=:msgId and m.id.userId=:userId")
    Boolean existsByMessageIdAndUserId(@Param("msgId")long msgId, @Param("userId")Long receiverId);
}
