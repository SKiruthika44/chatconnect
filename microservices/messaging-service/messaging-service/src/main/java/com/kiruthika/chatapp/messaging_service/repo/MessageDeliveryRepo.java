package com.kiruthika.chatapp.messaging_service.repo;

import com.kiruthika.chatapp.messaging_service.modal.MessageDelivery;
import com.kiruthika.chatapp.messaging_service.modal.MessageUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository

public interface MessageDeliveryRepo extends JpaRepository<MessageDelivery, MessageUserId> {


    @Query(value = "select count(m)>0 from MessageDelivery m where m.id.messageId=:msgId and m.id.userId=:userId")
    Boolean existsByMessageIdAndUserId(@Param("msgId")long msgId, @Param("userId")long userId);
}
