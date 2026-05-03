package com.kiruthika.chatapp.messaging_service.repo;

import com.kiruthika.chatapp.messaging_service.modal.MessageReaction;
import com.kiruthika.chatapp.messaging_service.modal.MessageUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MessageReactionRepo extends JpaRepository<MessageReaction, MessageUserId> {

    @Query(value="Select exists (select 1 from MessageReaction r where r.id.messageId=:msgId and r.id.userId=:userId)")
    Boolean isReacted(@Param("msgId")long msgId, @Param("userId") Long userId);


    @Query(value="Select r from MessageReaction r where r.id.messageId=:msgId and r.id.userId=:userId")
    MessageReaction findByMsgIdAndUserId(@Param("msgId")long msgId, @Param("userId")Long userId);


    @Query(value="select r.emoji from MessageReaction r where r.id.messageId=:msgId")
    List<String> findEmojis(long msgId);

    @Query(value="select r.emoji,count(*) from MessageReaction r where r.id.messageId=:msgId group by r.emoji")
    List<Object[]> findEmojisCountForMessage(@Param("msgId")Long msgId);
}
