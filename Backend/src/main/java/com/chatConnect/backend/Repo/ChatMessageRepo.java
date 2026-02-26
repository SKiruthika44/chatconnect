package com.chatConnect.backend.Repo;

import com.chatConnect.backend.Modal.ChatMessage;
import com.chatConnect.backend.Modal.PrivateMessageEmojiReaction;
import com.chatConnect.backend.Modal.Users;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepo extends JpaRepository<ChatMessage,Long> {


    List<ChatMessage> findBySenderAndReceiver(Users user, Users otherUser, Sort sortOption);

    List<ChatMessage> findByReceiverAndIsReadFalse(Users username);



    List<ChatMessage> findByReceiverAndSenderAndIsReadFalse(Users receiver, Users sender);

    List<ChatMessage> findByReceiverAndDeliveredFalse(Users receiver);


    @Query(value = """
            select m from ChatMessage m where
            ((m.sender=:user and m.receiver=:otherUser) or (m.receiver=:user and m.sender=:otherUser)) 
            and not exists  (select 1 from PrivateMessageDeletedUser d
             where d.id.messageId=m.id and d.id.userId=:userId) order  by m.createdAt asc
            """)
    List<ChatMessage> findAllVisibleMessageBetweenSenderAndReceiver(@Param("user")Users user,@Param("otherUser")Users otherUser,@Param("userId")Long userId);


    @Query(value="Select r.emoji from PrivateMessageEmojiReaction r where r.id.messageId=:msgId")
    List<String> findEmojisByMessageId(@Param("msgId")Long msgId);

    @Query(value="select exists (select 1 from PrivateMessageEmojiReaction r where r.id.messageId=:msgId and r.id.userId=:userId)")

   Boolean existsMessageUserId(@Param("msgId")Long msgId,@Param("userId")Long userId);

    /*@Query(value="update PrivateMessageEmojiReaction set r.emoji=:emoji where r.id.messageId=:msgId and r.id.userId=:userId")

    void updateEmoji(@Param("msgId")Long msgId,@Param("userId")Long userId,@Param("emoji")String emoji);*/

    @Query(value="select r from PrivateMessageEmojiReaction r where r.id.messageId=:msgId and r.id.userId=:userId")

    PrivateMessageEmojiReaction findByMsgIdAndUserId(@Param("msgId")Long msgId,@Param("userId")Long userId);

    /*@Query(value="select r.emoji from PrivateMessageEmojiReaction r where r.id.messageId=:msgId ")

    List<String> findEmojisByMessageId(@Param("msgId")Long msgId);*/

}
