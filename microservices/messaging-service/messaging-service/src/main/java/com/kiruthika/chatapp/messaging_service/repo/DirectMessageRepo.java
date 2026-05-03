package com.kiruthika.chatapp.messaging_service.repo;

import com.kiruthika.chatapp.messaging_service.modal.DirectMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DirectMessageRepo extends JpaRepository<DirectMessage,Long> {

    List<DirectMessage> findByReceiverIdAndSenderId(Long receiverId, Long senderId);

    List<DirectMessage> findByReceiverId(Long userId);


    @Query(value="select m from DirectMessage m where (m.senderId=:userId1 and m.receiverId=:userId2) or (m.receiverId=:userId1 and m.senderId=:userId2) order by m.createdAt asc")
    List<DirectMessage> findAllMessagesBetweenSenderAndReceiver(@Param("userId1")Long userId, @Param("userId2")Long otherUserId);


    @Query(value="select m from DirectMessage m where ((m.senderId=:userId1 and m.receiverId=:userId2) or (m.receiverId=:userId1 and m.senderId=:userId2)) and not exists (select 1 from MessageDeletion d where d.id.messageId=m.msgId and d.id.userId=:userId1) order by m.createdAt asc")
    List<DirectMessage> findAllVisibleMessagesBetweenSenderAndReceiver(@Param("userId1")Long userId, @Param("userId2")Long otherUserId);
}
